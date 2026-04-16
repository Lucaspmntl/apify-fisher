package com.lucas.scraper.exception;

import com.lucas.scraper.dto.out.exception.PollingFailedOut;
import lombok.Getter;

public class PollingFailedException extends RuntimeException {

    @Getter
    private final PollingFailedOut details;

    public PollingFailedException(String message, int statusCode, double timeSpent, int attempts ) {
        super(message);
        this.details = new PollingFailedOut(message, statusCode, timeSpent, attempts);
    }
}
