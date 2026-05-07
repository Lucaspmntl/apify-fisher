package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtProfileIn;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.service.ApifyGenericFeign;
import com.lucas.scraper.service.ApifyPolling;
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
        log.info("Tiktok Profile Gateway: Iniciando a Run de id {} para scraping de perfil", run.data().runId());

        if (!polling.waitForSucceeded(run.data().runId()))
            log.error("Tiktok Profile Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawProfiles = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Tiktok Profile Gateway: Run de id {} retornou {} itens", run.data().runId(), rawProfiles.size());

        // Transforma os dados List<Map<String, Object>> em um objeto TtProfileOut
        return rawProfiles.stream().map(raw -> {

            // Extração de dados aninhados em authorMeta
            Map<String, Object> authorMeta = (Map<String, Object>) raw.get("authorMeta");
            if (authorMeta != null) {
                return new TtProfileOut(
                        (String) authorMeta.get("id"),
                        (String) authorMeta.get("name"),
                        (String) authorMeta.get("profileUrl"),
                        (String) authorMeta.get("nickName"),
                        (String) authorMeta.get("signature"),
                        (String) authorMeta.get("avatar")
                );
            }
            return null;
        }).filter(profile -> profile != null).toList();
    }
}
