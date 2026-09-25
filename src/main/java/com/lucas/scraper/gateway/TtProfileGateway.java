package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtProfileIn;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import com.lucas.scraper.utils.RawItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TtProfileGateway {

    @Value("${apify.actor-id.tiktok-profile}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(TtProfileGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;
    public TtProfileGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<TtProfileOut> getProfile(TtProfileIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("TikTok Profile Gateway: Iniciando a Run de id {} para scraping de perfil", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawProfiles = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("TikTok Profile Gateway: Run de id {} retornou {} itens", run.data().runId(), rawProfiles.size());

        // Transforma os dados List<Map<String, Object>> em um objeto TtProfileOut
        return rawProfiles.stream().map(raw -> {

            // Extração do campo authorMeta
            Map<String, Object> authorMeta = RawItemMapper.getMap(raw, "authorMeta");

            if (authorMeta == null)
                return null;

            String id = RawItemMapper.getString(authorMeta, "id");
            String username = RawItemMapper.getString(authorMeta, "name");
            String fullName = RawItemMapper.getString(authorMeta, "nickName");
            String biography = RawItemMapper.getString(authorMeta, "signature");
            String profilePicUrl = RawItemMapper.getString(authorMeta, "avatar");
            String profileUrl = RawItemMapper.getString(authorMeta, "profileUrl");
            Boolean isPrivate = RawItemMapper.getBoolean(authorMeta, "privateAccount");

                return new TtProfileOut(
                        id,
                        username,
                        fullName,
                        biography,
                        profilePicUrl,
                        profileUrl,
                        isPrivate
                );
        }).filter(profile -> profile != null).toList();
    }
}
