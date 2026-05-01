package com.lucas.scraper.exception;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;

public class GenericCoreException extends RuntimeException {

    private final GenericMessageOut details;

    public GenericCoreException(String message, int statusCode) {
        super(message);
        this.details = new GenericMessageOut(message, statusCode);
    }

    public GenericMessageOut getDetails() {
        return details;
    }
}
