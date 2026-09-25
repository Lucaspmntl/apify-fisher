package com.lucas.scraper.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ApifyFeignConfig {

    // ponytail: timeout fixo, sem retry/backoff — trocar por resilience4j se o Apify passar a
    // exigir retry automático em falha de rede
    @Bean
    public Request.Options apifyRequestOptions() {
        return new Request.Options(5000, TimeUnit.MILLISECONDS, 30000, TimeUnit.MILLISECONDS, true);
    }
}
