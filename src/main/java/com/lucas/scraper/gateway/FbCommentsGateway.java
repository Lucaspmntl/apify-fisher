package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import com.lucas.scraper.utils.RawItemMapper;
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

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Facebook Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto FbCommentsOut
        return rawComments.stream().map(raw -> {

            String id = RawItemMapper.getString(raw, "commentId");
            String text = RawItemMapper.getString(raw, "text");
            OffsetDateTime date = RawItemMapper.getIsoDate(raw, "date");

            String ownerUsername = RawItemMapper.getString(raw, "profileName");
            String ownerId = RawItemMapper.getString(raw, "profileId");
            String ownerPicUrl = RawItemMapper.getString(raw, "profilePicture");


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
