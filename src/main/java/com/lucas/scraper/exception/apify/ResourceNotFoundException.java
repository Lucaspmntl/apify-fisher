package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class ResourceNotFoundException extends GenericCoreException {

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
