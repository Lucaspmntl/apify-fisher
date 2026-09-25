package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbProfileIn;
import com.lucas.scraper.dto.out.FbProfileOut;
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
public class FbProfileGateway {

    @Value("${apify.actor-id.facebook-profile}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(FbProfileGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;

    public FbProfileGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<FbProfileOut> getFacebookProfile(FbProfileIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Facebook Profile Gateway: Iniciando a Run de id {} para scraping de perfil", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawProfiles = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Facebook Profile Gateway: Run de id {} retornou {} itens", run.data().runId(), rawProfiles.size());

        // Transforma os dados List<Map<String, Object>> em um objeto FbProfileOut
        return rawProfiles.stream().map(raw -> {


            // Extração de profile
            Map<String, Object> profile = RawItemMapper.getMap(raw, "profile");
            if (profile == null) {
                return null;
            }

            String id = RawItemMapper.getString(profile, "profile_id");
            String username = RawItemMapper.getString(profile, "name");
            String biography = RawItemMapper.getString(profile, "intro");
            String profilePicUrl = RawItemMapper.getString(profile, "image");
            String profileUrl = RawItemMapper.getString(profile, "url");

            return new FbProfileOut(
                id,
                username,
                biography,
                profilePicUrl,
                profileUrl
            );
        }).filter(profile -> profile != null).toList();

    }
}
