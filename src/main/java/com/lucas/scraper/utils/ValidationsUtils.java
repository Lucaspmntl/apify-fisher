package com.lucas.scraper.utils;

import com.lucas.scraper.exception.InvalidResponseDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ValidationsUtils {

    static final Logger log = LoggerFactory.getLogger(ValidationsUtils.class);

    public static void parseDataVality(List<? extends ValidatablePayload> response){
        boolean isValid = response != null && !response.isEmpty() && !response.getFirst().isBlankPayload();

        if(!isValid){
            log.error("Facebook Service: Dados vazios ou nulos. Ocorreu um erro externo impedindo a coleta dos dados do post.");
            throw new InvalidResponseDataException("Não foi possível coletar os dados do post por serem inválidos ou nulos.");
        }
    }

    // TODO: Criar um método para validar URL's
}
