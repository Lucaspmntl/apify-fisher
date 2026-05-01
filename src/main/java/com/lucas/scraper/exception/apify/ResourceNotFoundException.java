package com.lucas.scraper.exception.apify;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.exception.GenericCoreException;
import lombok.Getter;

public class ResourceNotFoundException extends GenericCoreException {

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}
