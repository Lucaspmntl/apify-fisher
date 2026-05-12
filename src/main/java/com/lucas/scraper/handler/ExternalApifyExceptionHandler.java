package com.lucas.scraper.handler;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.exception.apify.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExternalApifyExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public GenericMessageOut resourceNotFoundException(ResourceNotFoundException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler(InvalidInputException.class)
    public GenericMessageOut invalidInputException(InvalidInputException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler(InsufficientPermissionsException.class)
    public GenericMessageOut insufficientPermissionsException(InsufficientPermissionsException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public GenericMessageOut methodNotAllowedException(MethodNotAllowedException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler(RateLimitExceedException.class)
    public GenericMessageOut rateLimitExceedException(RateLimitExceedException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public GenericMessageOut invalidTokenException(InvalidTokenException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler(DefaultIntegrationException.class)
    public GenericMessageOut defaultIntegrationException(DefaultIntegrationException e){
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }
}
