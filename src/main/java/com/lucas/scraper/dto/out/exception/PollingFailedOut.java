package com.lucas.scraper.dto.out.exception;

import java.time.OffsetDateTime;

public record PollingFailedOut (
    String message,
    int statusCode,
    String attemptsTime,
    int attempts,
    String timestamp
) {
    public PollingFailedOut(String message, int statusCode, String attemptsTime, int attempts) {
        this(message, statusCode, attemptsTime, attempts, OffsetDateTime.now().toString());
    }
}
