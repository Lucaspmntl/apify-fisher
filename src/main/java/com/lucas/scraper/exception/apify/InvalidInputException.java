package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class InvalidInputException extends GenericCoreException {

    public InvalidInputException(String message) {
        super(message, 400);
    }
}
