package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgPostIn;
import com.lucas.scraper.dto.out.IgPostOut;
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

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Instagram Post Gateway: Iniciando a Run de id {} para scraping de post", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawPosts = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Instagram Post Gateway: Run de id {} retornou {} itens", run.data().runId(), rawPosts.size());

        // Transforma os dados List<Map<String, Object>> em um objeto IgPostOut
        return rawPosts.stream().map(raw ->{

            String id = RawItemMapper.getString(raw, "id");
            String description = RawItemMapper.getString(raw, "caption");
            OffsetDateTime date = RawItemMapper.getIsoDate(raw, "timestamp");
            Integer commentsCount = RawItemMapper.getInteger(raw, "commentsCount");
            Integer likesCount = RawItemMapper.getInteger(raw, "likesCount"); // RETORNARÁ -1 CASO LIKES SÓ SEJAM VISIVEIS PARA OWNER
            String imageUrl = RawItemMapper.getString(raw, "displayUrl");
            String postUrl = RawItemMapper.getString(raw, "url");
            String videoUrl = RawItemMapper.getString(raw, "videoUrl"); // RETORNA NULL CASO NÃO SEJA UM VíDEO

            String ownerFullName = RawItemMapper.getString(raw, "ownerFullName");
            String ownerUsername = RawItemMapper.getString(raw, "ownerUsername");
            String ownerId = RawItemMapper.getString(raw, "ownerId");

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
