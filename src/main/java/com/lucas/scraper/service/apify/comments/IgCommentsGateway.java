package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
import com.lucas.scraper.exception.PollingFailedException;
import com.lucas.scraper.service.apify.ApifyGenericFeign;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import tools.jackson.databind.ObjectMapper;

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
    public IgCommentsGateway(ApifyGenericFeign apifyClient) {
        this.apifyClient = apifyClient;
    }

    public List<IgCommentsOut> getComments(IgCommentsIn input) {

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Gateway: Iniciando a Run de id {}", run.data().runId());


        if (!polling(run.data().runId()))
            log.error("Gateway: Polling falhou.");

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto IgCommentsInput
        return rawComments.stream().map(item -> new IgCommentsOut(
                (String) item.get("id"),
                (String) item.get("text"),
                (String) item.get("ownerUsername"),
                (String) item.get("ownerProfilePicUrl"),
                (String) item.get("timestamp"),
                (Integer) item.get("likesCount"),
                (Integer) item.get("repliesCount")

                //(List<repliesDTO>) item.get("replies")
        )).toList();

    }

    private boolean polling(String runId){
        log.debug("Gateway: Iniciando polling para requisição de id: {}", runId);

        String status;
        int attempts = 1;
        StopWatch watch = new StopWatch();
        watch.start();
        do{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                watch.stop();

                log.error("Erro na tentativa {} de polling: {}", attempts, e.toString());
                throw new PollingFailedException("Erro interno na tentativa de polling.",
                        500,
                        watch.getTotalTimeSeconds(),
                        attempts);
            }

            status = apifyClient.getRunDetails(runId, "Bearer " + token).data().status();

            log.debug("Gateway: Tentativa {} de polling, status atual: {}", attempts, status);

            // TODO: Tratar excessões como FAILED ou TIMED-OUT
            // Verificar as possibilidades em: https://docs.apify.com/api/v2/actor-run-get

            attempts++;
        } while(!status.equalsIgnoreCase("SUCCEEDED"));

        return true;
    }
}
