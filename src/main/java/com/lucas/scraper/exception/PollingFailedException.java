package com.lucas.scraper.exception;

public class PollingFailedException extends RuntimeException {
    public PollingFailedException(String message) {
        super(message);
    }
}
