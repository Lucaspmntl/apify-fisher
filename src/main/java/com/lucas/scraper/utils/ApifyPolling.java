package com.lucas.scraper.utils;

import com.lucas.scraper.exception.PollingFailedException;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.Set;

@Service
public class ApifyPolling {

    // ponytail: limite fixo de tentativas (5min), sem backoff exponencial — trocar se o Apify passar a
    // demorar mais que isso rotineiramente
    public static final int MAX_ATTEMPTS = 60;
    private static final Set<String> FAILURE_STATUSES = Set.of("FAILED", "ABORTED", "TIMED-OUT");

    @Value("${apify.token}")
    String token;

    private final ApifyGenericFeign apifyClient;
    private static final Logger log = LoggerFactory.getLogger(ApifyPolling.class);

    public ApifyPolling(ApifyGenericFeign apifyClient) {
        this.apifyClient = apifyClient;
    }

    @SneakyThrows
    public void waitForSucceeded(String runId) {
        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Polling: Iniciando polling para requisição de id: {}", runId);

        String status;
        int attempts = 1;

        do{
            Thread.sleep(5000);

            status = apifyClient.getRunDetails(runId, "Bearer " + token).data().status();

            log.debug("Gateway: Tentativa {} de polling, status atual: {}", attempts, status);

            if (FAILURE_STATUSES.contains(status.toUpperCase())) {
                watch.stop();
                log.error("Polling: Run {} terminou com status {} após {} tentativas", runId, status, attempts);
                throw new PollingFailedException(
                        "A run do Apify terminou com status " + status + ".", 502, watch.getTotalTimeSeconds(), attempts);
            }

            if (attempts >= MAX_ATTEMPTS) {
                watch.stop();
                log.error("Polling: Run {} excedeu o limite de {} tentativas", runId, MAX_ATTEMPTS);
                throw new PollingFailedException(
                        "Tempo limite de polling excedido para a run do Apify.", 504, watch.getTotalTimeSeconds(), attempts);
            }

            attempts++;

        } while(!status.equalsIgnoreCase("SUCCEEDED"));

        watch.stop();
        log.info("Polling: Polling concluído em {}s", watch.getTotalTimeSeconds());
    }


    /*
        Retorna true se o status da run for "RUNNING".
     */
    public boolean isStatusRunning(String runId){

        return apifyClient.getRunDetails(runId, "Bearer " + token)
                .data()
                .status()
                .equalsIgnoreCase("RUNNING");
    }
}
