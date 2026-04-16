package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class MethodNotAllowedException extends GenericCoreException {

    public MethodNotAllowedException(String message) {
        super(message, 405);
    }
}
