package com.lucas.scraper.service.apify;

import com.lucas.scraper.exception.PollingFailedException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class ApifyPolling {

    @Value("${apify.token}")
    String token;

    private final ApifyGenericFeign apifyClient;
    private static final Logger log = LoggerFactory.getLogger(ApifyPolling.class);

    public ApifyPolling(ApifyGenericFeign apifyClient) {
        this.apifyClient = apifyClient;
    }

    @SneakyThrows
    public boolean waitForSucceeded(String runId) {
        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Polling: Iniciando polling para requisição de id: {}", runId);

        String status;
        int attempts = 1;

        do{
            Thread.sleep(5000);
                watch.stop();

            status = apifyClient.getRunDetails(runId, "Bearer " + token).data().status();

            log.debug("Gateway: Tentativa {} de polling, status atual: {}", attempts, status);

            attempts++;

            // Não há tratamento de erro no tempo total do polling, caso ocorra de exceder o rate limit existe
            // uma exception própria pra isso

        } while(!status.equalsIgnoreCase("SUCCEEDED"));

        watch.stop();
        log.info("Polling: Polling concluído em {}s", watch.getTotalTimeSeconds());
        return true;
    }
}
