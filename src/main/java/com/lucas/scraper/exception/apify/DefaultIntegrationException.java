package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class DefaultIntegrationException extends GenericCoreException {

    public DefaultIntegrationException(String message, int statusCode) {
        super(message, statusCode);
    }
}
