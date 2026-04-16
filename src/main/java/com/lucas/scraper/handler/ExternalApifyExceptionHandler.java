package com.lucas.scraper.handler;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.exception.apify.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExternalApifyExceptionHandler {

    @ExceptionHandler
    public GenericMessageOut resourceNotFoundException(ResourceNotFoundException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler
    public GenericMessageOut invalidInputException(InvalidInputException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler
    public GenericMessageOut insufficientPermissionsException(InsufficientPermissionsException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler
    public GenericMessageOut methodNotAllowedException(MethodNotAllowedException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler
    public GenericMessageOut rateLimitExceedException(RateLimitExceedException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler
    public GenericMessageOut invalidTokenException(InvalidTokenException e) {
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }

    @ExceptionHandler
    public GenericMessageOut defaultIntegrationException(DefaultIntegrationException e){
        return new GenericMessageOut(e.getMessage(), e.getDetails().statusCode());
    }
}
