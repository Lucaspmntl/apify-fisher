package com.lucas.scraper.exception.apify;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.exception.GenericCoreException;
import lombok.Getter;

public class RateLimitExceedException extends GenericCoreException {

    public RateLimitExceedException(String message) {
        super(message, 429);
    }
}
