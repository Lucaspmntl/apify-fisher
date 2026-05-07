package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
import com.lucas.scraper.service.ApifyGenericFeign;
import com.lucas.scraper.service.ApifyPolling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Date;
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

    public List<IgCommentsOut> getInstagramComments(IgCommentsIn input) {

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
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
}
