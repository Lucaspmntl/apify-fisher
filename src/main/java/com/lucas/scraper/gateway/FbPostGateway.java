package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbPostIn;
import com.lucas.scraper.dto.out.FbPostOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Component
public class FbPostGateway {

    @Value("${apify.actor-id.facebook-post}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(FbPostGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;

    public FbPostGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<FbPostOut> getFacebookPost(FbPostIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Facebook Post Gateway: Iniciando a Run de id {} para scraping de post", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawPosts = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Facebook Post Gateway: Run de id {} retornou {} itens", run.data().runId(), rawPosts.size());

        // Transforma os dados List<Map<String, Object>> em um objeto FbPostOut
        return rawPosts.stream().map(raw -> {

            // Dados não aninhados
            String id = (String) raw.get("id");
            String description = (String) raw.get("previewDescription");
            OffsetDateTime date = OffsetDateTime.ofInstant(Instant.ofEpochSecond((Integer) raw.get("publish_time")), ZoneId.systemDefault());
            String postUrl = (String) raw.get("facebookUrl");

            // Extração de username dentro de URI da publicação (A API não disponibiliza o respectivo dado)
            String ownerUsername = URI.create(postUrl).getPath().split("/")[1];

            // Extração de imageUrl em preferred_thumbnail.image.uri
            String imageUrl = null;
            Map<String, Object> preferredThumbnail = (Map<String, Object>) raw.get("preferred_thumbnail");
            if (preferredThumbnail != null) {
                Map<String, Object> image = (Map<String, Object>) preferredThumbnail.get("image");
                if (image != null) {
                    imageUrl = (String) image.get("uri");
                }
            }
            
            // Extração de ownerId em owner.id
            String ownerId = null;
            Map<String, Object> owner = (Map<String, Object>) raw.get("owner");
            if (owner != null) {
                ownerId = (String) owner.get("id");
            }
            
            // Extração do reactionCount em reaction_count.count
            Integer reactionCount = null;
            Map<String, Object> reactionCountObj = (Map<String, Object>) raw.get("reaction_count");
            if (reactionCountObj != null) {
                reactionCount = (Integer) reactionCountObj.get("count");
            }
            
            // Extract commentCount dentro de comment_rendering_instance.comments
            Integer commentCount = null;
            Map<String, Object> commentRenderingInstance = (Map<String, Object>) raw.get("comment_rendering_instance");
            if (commentRenderingInstance != null) {
                Map<String, Object> comments = (Map<String, Object>) commentRenderingInstance.get("comments");
                if (comments != null) {
                    commentCount = (Integer) comments.get("total_count");
                }
            }
            
            return new FbPostOut(
                    id,
                    description,
                    date,
                    commentCount,
                    reactionCount,
                    imageUrl,
                    postUrl,
                    ownerUsername,
                    ownerId
            );
        }).toList();

    }
}
