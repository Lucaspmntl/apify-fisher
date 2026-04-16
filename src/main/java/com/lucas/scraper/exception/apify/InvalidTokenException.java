package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class InvalidTokenException extends GenericCoreException {
    public InvalidTokenException(String message) {
        super(message, 401);
    }
}
