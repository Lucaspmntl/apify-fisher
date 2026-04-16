package com.lucas.scraper.exception;

public class InvalidURLException extends GenericCoreException {

    public InvalidURLException(String message) {
        super(message, 400);
    }
}
