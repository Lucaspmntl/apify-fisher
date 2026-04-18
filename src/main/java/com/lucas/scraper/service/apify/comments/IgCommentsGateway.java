package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
import com.lucas.scraper.exception.PollingFailedException;
import com.lucas.scraper.service.apify.ApifyGenericFeign;
import com.lucas.scraper.service.apify.ApifyPolling;
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
    private final ApifyPolling polling;
    public IgCommentsGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<IgCommentsOut> getComments(IgCommentsIn input) {

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Gateway: Iniciando a Run de id {}", run.data().runId());


        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Gateway: Polling falhou para requisição de id {}.", run.data().runId());

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
}
