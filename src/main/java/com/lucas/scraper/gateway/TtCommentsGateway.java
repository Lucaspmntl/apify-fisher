package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.dto.out.startRun.RunResponseOut;
import com.lucas.scraper.service.ApifyGenericFeign;
import com.lucas.scraper.service.ApifyPolling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Date;
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

        RunResponseOut run = apifyClient.startRun(actorId, input, "Bearer " + token);
        log.info("Tiktok Gateway: Iniciando a Run de id {} para scraping de comentários", run.data().runId());

        if(!polling.waitForSucceeded(run.data().runId()))
            log.error("Tiktok Gateway: Polling falhou para requisição de id {}.", run.data().runId());

        List<Map<String, Object>> rawComments = apifyClient.getDatasetItems(run.data().runId(), "Bearer " + token);
        log.info("Tiktok Gateway: Run de id {} retornou {} itens", run.data().runId(), rawComments.size());

        // Transforma os dados List<Map<String, Object>> em um objeto TtCommentsOut
        return rawComments.stream().map(raw -> new TtCommentsOut(
                (String) raw.get("text"),
                OffsetDateTime.parse((String) raw.get("createTimeISO")),
                (String) raw.get("uniqueId"),
                (String) raw.get("uid"),
                (String) raw.get("cid"),
                (String) raw.get("avatarThumbnail")
        )).toList();
    }
}
