package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
import com.lucas.scraper.service.ApifyGenericFeign;
import com.lucas.scraper.service.ApifyPolling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class FbCommentsGateway {

    @Value("${apify.actor-id.facebook-comment}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(FbCommentsGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;
    public FbCommentsGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<FbCommentsOut> getComments(FbCommentsIn input) {

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Facebook Gateway: Iniciando a Run de id {} para scraping de comentários", run.data().runId());

        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Facebook Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Facebook Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto FbCommentsOut
        return rawComments.stream().map(raw -> new FbCommentsOut(
                (String) raw.get("id"),
                (String) raw.get("feedbackId"),
                (Date) raw.get("date"),
                (String) raw.get("text"),
                (String) raw.get("profilePicture"),
                (String) raw.get("profileId"),
                (String) raw.get("likesCount"),
                (String) raw.get("facebookId"),
                (String) raw.get("inputUrl")
        )).toList();

    }
}
