package com.lucas.scraper.dto.out.exception;

import java.time.OffsetDateTime;

public record PollingFailedOut (
    String message,
    int statusCode,
    double timeSpentSeconds,
    int attempts,
    String timestamp
) {
    public PollingFailedOut(String message, int statusCode, double timeSpent, int attempts) {
        this(message, statusCode, timeSpent, attempts, OffsetDateTime.now().toString());
    }
}
