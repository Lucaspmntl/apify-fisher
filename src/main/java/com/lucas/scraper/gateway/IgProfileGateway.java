package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgProfileIn;
import com.lucas.scraper.dto.out.IgProfileOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
import com.lucas.scraper.service.ApifyGenericFeign;
import com.lucas.scraper.service.ApifyPolling;
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

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Instagram Profile Gateway: Iniciando a Run de id {} para scraping de perfil", run.data().runId());

        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Instagram Profile Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawProfiles = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Instagram Profile Gateway: Run de id {} retornou {} itens", run.data().runId(), rawProfiles.size());

        // Transforma os dados List<Map<String, Object>> em um objeto IgProfileOut
        return rawProfiles.stream().map(raw -> new IgProfileOut(
                (String) raw.get("id"),
                (String) raw.get("username"),
                (String) raw.get("biography"),
                (String) raw.get("profilePicUrl"),
                (String) raw.get("profilePicUrlHD"),
                (Boolean) raw.get("private"),
                (String) raw.get("fullName"),
                (String) raw.get("url")
        )).toList();

    }
}
