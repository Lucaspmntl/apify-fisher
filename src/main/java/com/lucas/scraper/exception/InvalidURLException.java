package com.lucas.scraper.exception;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;

public class InvalidURLException extends RuntimeException {
    public InvalidURLException(String message) {
        super(message);
    }
}
