package com.lucas.scraper.exception;

public class InvalidResponseDataException extends GenericCoreException {
    public InvalidResponseDataException(String message) {
        super(message, 404);
    }
}
