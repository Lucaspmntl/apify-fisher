package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class InsufficientPermissionsException extends GenericCoreException {
    public InsufficientPermissionsException(String message) {
        super(message, 403);
    }
}
