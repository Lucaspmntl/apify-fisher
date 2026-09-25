package com.lucas.scraper.handler;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.exception.apify.InvalidTokenException;
import com.lucas.scraper.exception.apify.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExternalApifyExceptionHandlerTest {

    private final ExternalApifyExceptionHandler handler = new ExternalApifyExceptionHandler();

    @Test
    void genericCoreException_returnsRealHttpStatus_notAlways200() {
        // Regressão: antes o handler devolvia o DTO puro (sem ResponseEntity), então o Spring
        // respondia sempre HTTP 200 mesmo para um erro 401 vindo do Apify
        ResponseEntity<GenericMessageOut> response = handler.genericCoreException(new InvalidTokenException("token inválido"));

        assertEquals(401, response.getStatusCode().value());
        assertEquals(401, response.getBody().statusCode());
    }

    @Test
    void genericCoreException_mapsResourceNotFound_to404() {
        ResponseEntity<GenericMessageOut> response = handler.genericCoreException(new ResourceNotFoundException("não encontrado"));

        assertEquals(404, response.getStatusCode().value());
    }
}
