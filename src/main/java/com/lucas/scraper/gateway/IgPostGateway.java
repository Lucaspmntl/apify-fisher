package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgPostIn;
import com.lucas.scraper.dto.out.IgPostOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
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
public class IgPostGateway {

    @Value("${apify.actor-id.instagram-post}")
    String actorId;

    @Value("${apify.token}")
    String token;

    public static final Logger log = LoggerFactory.getLogger(IgPostGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;
    public IgPostGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<IgPostOut> getInstagramPost(IgPostIn input) {

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Instagram Post Gateway: Iniciando a Run de id {} para scraping de post", run.data().runId());

        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Instagram Post Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawPosts = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Instagram Post Gateway: Run de id {} retornou {} itens", run.data().runId(), rawPosts.size());

        // Transforma os dados List<Map<String, Object>> em um objeto IgPostOut
        return rawPosts.stream().map(raw ->{

            String id = (String) raw.get("id");
            String description = (String) raw.get("caption");
            OffsetDateTime date = OffsetDateTime.parse((String) raw.get("timestamp"));
            Integer commentsCount = (Integer) raw.get("commentsCount");
            Integer likesCount = (Integer) raw.get("likesCount"); // RETORNARÁ -1 CASO LIKES SÓ SEJAM VISIVEIS PARA OWNER
            String imageUrl = (String) raw.get("displayUrl");
            String postUrl = (String) raw.get("url");
            String videoUrl = (String) raw.get("videoUrl"); // RETORNA NULL CASO NÃO SEJA UM VíDEO

            String ownerFullName = (String) raw.get("ownerFullName");
            String ownerUsername = (String) raw.get("ownerUsername");
            String ownerId = (String) raw.get("ownerId");

            return new IgPostOut(
                    id,
                    description,
                    date,
                    commentsCount,
                    likesCount,
                    imageUrl,
                    postUrl,
                    videoUrl,

                    ownerFullName,
                    ownerUsername,
                    ownerId
            );
        }
        ).filter(post -> post.postUrl() != null).toList();

    }
}
