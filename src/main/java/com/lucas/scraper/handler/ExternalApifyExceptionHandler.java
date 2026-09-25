package com.lucas.scraper.handler;

import com.lucas.scraper.dto.out.exception.GenericMessageOut;
import com.lucas.scraper.exception.GenericCoreException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExternalApifyExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExternalApifyExceptionHandler.class);

    // Cobre todas as exceptions de exception/apify/* (todas estendem GenericCoreException e já carregam
    // o statusCode correto vindo do GlobalFeignDecoder)
    @ExceptionHandler(GenericCoreException.class)
    public ResponseEntity<GenericMessageOut> genericCoreException(GenericCoreException e) {
        return ResponseEntity.status(e.getDetails().statusCode()).body(e.getDetails());
    }

    // Falha de rede (timeout, conexão recusada) antes de qualquer resposta HTTP do Apify chegar —
    // não passa pelo GlobalFeignDecoder, que só decodifica respostas já recebidas.
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<GenericMessageOut> feignException(FeignException e) {
        log.error("Falha de comunicação com o Apify: {}", e.getMessage());
        return ResponseEntity.status(502).body(new GenericMessageOut("Falha de comunicação com o Apify.", 502));
    }
}
