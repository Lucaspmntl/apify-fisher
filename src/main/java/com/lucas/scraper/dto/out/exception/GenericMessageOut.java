package com.lucas.scraper.dto.out.exception;


import java.time.OffsetDateTime;

public record GenericMessageOut (
        String message,
        int statusCode,
        String timestamp
) {
    public GenericMessageOut(String message, int statusCode) {
        this(message, statusCode, OffsetDateTime.now().toString());
    }
}
