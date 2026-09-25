package com.lucas.scraper.exception.apify;

import com.lucas.scraper.exception.GenericCoreException;

public class RateLimitExceedException extends GenericCoreException {

    public RateLimitExceedException(String message) {
        super(message, 429);
    }
}
