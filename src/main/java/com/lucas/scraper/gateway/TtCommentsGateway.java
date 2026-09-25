package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
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
public class TtCommentsGateway {

    @Value("${apify.actor-id.tiktok-comment}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(TtCommentsGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;
    public TtCommentsGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<TtCommentsOut> getComments(TtCommentsIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Tiktok Gateway: Iniciando a Run de id {} para scraping de comentários", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Tiktok Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto TtCommentsOut
        return rawComments.stream().map(raw -> {

            String id = RawItemMapper.getString(raw, "cid");
            String text = RawItemMapper.getString(raw, "text");
            String ownerUsername = RawItemMapper.getString(raw, "uniqueId");
            String ownerPicUrl = RawItemMapper.getString(raw, "avatarThumbnail");
            String ownerId = RawItemMapper.getString(raw, "uid");
            OffsetDateTime date = RawItemMapper.getIsoDate(raw, "createTimeISO");

            return new TtCommentsOut(
                id,
                text,
                date,

                ownerUsername,
                ownerId,
                ownerPicUrl
            );

        }).filter(comments -> comments != null).toList();
    }
}
