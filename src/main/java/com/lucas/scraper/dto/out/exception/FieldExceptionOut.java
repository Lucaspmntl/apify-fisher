package com.lucas.scraper.dto.out.exception;

public record FieldExceptionOut(
        String field,
        String message,
        Object rejectedValue
) {

    public FieldExceptionOut(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }
}
