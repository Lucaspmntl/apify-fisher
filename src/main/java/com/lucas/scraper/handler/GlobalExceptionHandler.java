package com.lucas.scraper.handler;

import com.lucas.scraper.dto.out.exception.BeansValidationOut;
import com.lucas.scraper.dto.out.exception.FieldExceptionOut;
import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.dto.out.exception.PollingFailedOut;
import com.lucas.scraper.exception.InvalidResponseDataException;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.exception.PollingFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<BeansValidationOut> MethodArgumentNotValidHandler(MethodArgumentNotValidException exception) {


        List<FieldError> exceptionField = exception.getBindingResult().getFieldErrors();
        List<FieldExceptionOut> fieldExceptionOut = new ArrayList<>();

        for (FieldError exceptionFields : exceptionField) {
            String field = exceptionFields.getField();
            String message = exceptionFields.getDefaultMessage();
            Object rejectedValue = exceptionFields.getRejectedValue();

            fieldExceptionOut.add(new FieldExceptionOut(field, message, rejectedValue));
        }

        BeansValidationOut response =
                new BeansValidationOut("Erro de validação, verifique os campos enviados.",
                        exception.getStatusCode().value(),
                        fieldExceptionOut);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidURLException.class)
    private ResponseEntity<GenericMessageOut> InvalidUrlExceptionHandler(InvalidURLException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getDetails());
    }

    @ExceptionHandler(InvalidResponseDataException.class)
    private ResponseEntity<GenericMessageOut> InvalidResponseDataExceptionHandler(InvalidResponseDataException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getDetails());
    }

    @ExceptionHandler(PollingFailedException.class)
    private ResponseEntity<PollingFailedOut> PollingFailedExceptionHandler(PollingFailedException exception) {
        return ResponseEntity.status(exception.getDetails().statusCode()).body(exception.getDetails());
    }
}
