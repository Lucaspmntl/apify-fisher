package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtPostIn;
import com.lucas.scraper.dto.out.TtPostOut;
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
public class TtPostGateway {

    @Value("${apify.actor-id.tiktok-post}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(TtPostGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;

    public TtPostGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<TtPostOut> getPost(TtPostIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Tiktok Post Gateway: Iniciando a Run de id {} para scraping de post", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawPosts = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Tiktok Post Gateway: Run de id {} retornou {} itens", run.data().runId(), rawPosts.size());

        // Transforma os dados List<Map<String, Object>> em um objeto TtPostOut
        return rawPosts.stream().map(raw -> {

            // Dados não aninhados
            String id = RawItemMapper.getString(raw, "id");
            String description = RawItemMapper.getString(raw, "text");
            OffsetDateTime date = RawItemMapper.getIsoDate(raw, "createTimeISO");
            Integer commentsCount = RawItemMapper.getInteger(raw, "commentCount");
            Integer likesCount = RawItemMapper.getInteger(raw, "diggCount");
            String postUrl = RawItemMapper.getString(raw, "webVideoUrl");

            // Extração de ownerId em authorMeta.id
            Map<String, Object> authorMeta = RawItemMapper.getMap(raw, "authorMeta");

            String ownerId = null;
            String ownerUsername = null;
            String ownerFullName = null;
            if (authorMeta != null) {
                ownerId = RawItemMapper.getString(authorMeta, "id");
                ownerUsername = RawItemMapper.getString(authorMeta, "nickName");
                ownerFullName = RawItemMapper.getString(authorMeta, "name");
            }

            // Extração de imageUrl em videoMeta.coverUrl
            String imageUrl = null;
            Map<String, Object> videoMeta = RawItemMapper.getMap(raw, "videoMeta");
            if (videoMeta != null) {
                imageUrl = RawItemMapper.getString(videoMeta, "coverUrl");
            }

            return new TtPostOut(
                    id,
                    description,
                    date,
                    commentsCount,
                    likesCount,
                    postUrl,
                    imageUrl,

                    ownerId,
                    ownerUsername,
                    ownerFullName
            );
        }).toList();
    }
}
