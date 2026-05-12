package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.IgFisherDeltaCommentsIn;
import com.lucas.scraper.dto.in.apify.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsDeltaOut;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Component
public class IgCommentsGateway {

    @Value("${apify.actor-id.instagram-comment}")
    String actorId;

    @Value("${apify.token}")
    String token;

    public static final Logger log = LoggerFactory.getLogger(IgCommentsGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;
    public IgCommentsGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }


    public List<IgCommentsOut> getInstagramComments(IgCommentsIn data) {

        RunOut run = apifyClient.startRun(actorId, data, "Bearer " + token);
        log.info("Instagram Gateway: Iniciando a Run de id {} para scraping de comentários", run.data().runId());

        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Instagram Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Instagram Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto IgCommentsInput
        return rawComments.stream().map(raw -> {

            // Dados não aninhados
            String id = (String) raw.get("id");
            String text = (String) raw.get("text");
            String ownerPicUrl = (String) raw.get("ownerProfilePicUrl");
            OffsetDateTime date = OffsetDateTime.parse((String) raw.get("timestamp"));

            // Campo aninhado da response original
            Map<String, Object> owner = (Map<String, Object>) raw.get("owner");

            // Extração de campos dentro de owner
            String ownerId = null;
            String ownerUsername = null;
            if (owner != null){
                ownerId = (String) owner.get("id");
                ownerUsername = (String) owner.get("full_name");
            }

            return new IgCommentsOut(
                    id,
                    text,
                    date,

                    ownerUsername,
                    ownerId,
                    ownerPicUrl
            );
        }).filter(comment -> comment != null).toList();
    }


    @SneakyThrows
    public IgCommentsDeltaOut getDeltaInstagramComments(IgFisherDeltaCommentsIn data) {

        List<String> commentsIds = data.lastCommentsIds();
        boolean deltaFound;

        // TODO: Levar esse input para o InstagramService
        IgCommentsIn input = new IgCommentsIn(
                List.of(data.postUrl()),
                false,
                false,
                data.resultsLimit());

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        String runId = run.data().runId();

        log.info("Instagram Gateway: Iniciando a Run de id {} para scraping PARCIAL de comentários", run.data().runId());

        List<Map<String, Object>> rawComments = null;
        String runStatus;
        int parseAttempts = 1;

        do {

            log.info("Instagram Gateway: Iniciando tentativa {} de encontrar comentários de id's {} em run {}",
                    parseAttempts,
                    commentsIds.toString(),
                    runId);

            Pair<Boolean, List<Map<String, Object>>> parseDelta = parseDeltaComments(commentsIds, runId);
            deltaFound = parseDelta.getLeft();
            rawComments = parseDelta.getRight();

            // Caso comentário delta seja encontrado ele finaliza o loop aqui
            if (deltaFound)
                break;

            parseAttempts++;

            Thread.sleep(4000);
            runStatus = apifyClient.getRunDetails(runId, "Bearer " + token).data().status();

        // Quando a run for concluída ("SUCEEDDED") ou abortada ("ABORTED"), o loop termina
        } while (runStatus.equalsIgnoreCase("RUNNING"));


        List<IgCommentsOut> mappedComments = rawComments.stream().map(raw -> {

            // Dados não aninhados
            String id = (String) raw.get("id");
            String text = (String) raw.get("text");
            String ownerPicUrl = (String) raw.get("ownerProfilePicUrl");
            OffsetDateTime date = OffsetDateTime.parse((String) raw.get("timestamp"));

            // Campo aninhado da response original
            Map<String, Object> owner = (Map<String, Object>) raw.get("owner");

            // Extração de campos dentro de owner
            String ownerId = null;
            String ownerUsername = null;
            if (owner != null){
                ownerId = (String) owner.get("id");
                ownerUsername = (String) owner.get("full_name");
            }

            return new IgCommentsOut(
                    id,
                    text,
                    date,

                    ownerUsername,
                    ownerId,
                    ownerPicUrl
            );
        }).filter(comment -> comment != null).toList();

        return new IgCommentsDeltaOut(deltaFound, mappedComments);
    }


    private Pair<Boolean, List<Map<String, Object>>> parseDeltaComments(List<String> lastCommentsIds, String runId){

        boolean deltaFound = false;

        List<Map<String, Object>> currentScrapedComments = apifyClient.getDatasetItems(runId, "Bearer " + token);


        for (int i = 0; i < currentScrapedComments.size(); i++) {

            String currentId = (String) currentScrapedComments.get(i).get("id");
            boolean isFounded = lastCommentsIds.contains(currentId);

            if (isFounded) {
                log.info("Instagram Gateway: Comentário de id {} encontrado na posição {}, abortando run", currentId, i);

                deltaFound = true;
                apifyClient.abortRun(runId, "Bearer " + token);

                // Corta a lista de comentários até o comentário encontrado,
                // desconsiderando o comentário encontrado e comentários posteriores
                List<Map<String, Object>> slicedComments = currentScrapedComments.subList(0, i);

                // Retorna um par, sendo formado por um booleano informando se o delta foi encontrado e os comentários encontrados até então
                return Pair.of(deltaFound, slicedComments);
            }
        }

        log.warn("Instagram Gateway: Comentário de ID delta não encontrado, retonarndo lista de {} comentários encontrados até o momento.", currentScrapedComments.size());
        return Pair.of(deltaFound, currentScrapedComments);
    }
}
