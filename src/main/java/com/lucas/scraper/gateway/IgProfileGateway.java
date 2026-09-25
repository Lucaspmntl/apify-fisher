package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgProfileIn;
import com.lucas.scraper.dto.out.IgProfileOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class IgProfileGateway {

    @Value("${apify.actor-id.instagram-profile}")
    String actorId;

    @Value("${apify.token}")
    String token;

    public static final Logger log = LoggerFactory.getLogger(IgProfileGateway.class);
    private final ApifyGenericFeign apifyClient;
    private final ApifyPolling polling;
    public IgProfileGateway(ApifyGenericFeign apifyClient, ApifyPolling polling) {
        this.apifyClient = apifyClient;
        this.polling = polling;
    }

    public List<IgProfileOut> getInstagramProfile(IgProfileIn input) {

        RunOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Instagram Profile Gateway: Iniciando a Run de id {} para scraping de perfil", run.data().runId());

        polling.waitForSucceeded(run.data().runId());

        List<Map<String, Object>> rawProfiles = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Instagram Profile Gateway: Run de id {} retornou {} itens", run.data().runId(), rawProfiles.size());

        // Transforma os dados List<Map<String, Object>> em um objeto IgProfileOut
        return rawProfiles.stream().map(raw -> {

            String id = (String) raw.get("id");
            String username = (String) raw.get("username");
            String fullName = (String) raw.get("fullName");
            String biography = (String) raw.get("biography");
            String profilePicUrl = (String) raw.get("profilePicUrlHD");
            String profileUrl = (String) raw.get("url");
            Boolean isPrivate = (Boolean) raw.get("private");

            return new IgProfileOut(
                    id,
                    username,
                    fullName,
                    biography,
                    profilePicUrl,
                    profileUrl,
                    isPrivate
            );
        }
        ).filter(profile -> profile != null).toList();

    }
}
