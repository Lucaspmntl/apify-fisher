package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.service.ApifyGenericFeign;
import com.lucas.scraper.service.ApifyPolling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
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

    public List<FbCommentsOut> getFacebookComments(FbCommentsIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Facebook Gateway: Iniciando a Run de id {} para scraping de comentários", run.data().runId());

        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Facebook Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Facebook Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto FbCommentsOut
        return rawComments.stream().map(raw -> {

            String id = (String) raw.get("commentId");
            String text = (String) raw.get("text");
            OffsetDateTime date = OffsetDateTime.parse((String) raw.get("date"));

            String ownerUsername = (String) raw.get("profileName");
            String ownerId = (String) raw.get("profileId");
            String ownerPicUrl = (String) raw.get("profilePicture");


            return new FbCommentsOut(
                    id,
                    text,
                    date,

                    ownerUsername,
                    ownerId,
                    ownerPicUrl
            );
        }).filter(comment -> comment.id() != null).toList();

    }
}
