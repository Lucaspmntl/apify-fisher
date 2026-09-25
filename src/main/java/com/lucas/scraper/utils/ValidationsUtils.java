package com.lucas.scraper.utils;

import com.lucas.scraper.exception.InvalidResponseDataException;
import com.lucas.scraper.exception.InvalidURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ValidationsUtils {

    static final Logger log = LoggerFactory.getLogger(ValidationsUtils.class);

    public static void parseDataVality(List<? extends ValidatablePayload> response){
        boolean isValid = response != null && !response.isEmpty() && !response.getFirst().isBlankPayload();

        if(!isValid){
            log.error("Dados vazios ou nulos. Ocorreu um erro externo impedindo a coleta dos dados.");
            throw new InvalidResponseDataException("Não foi possível coletar os dados por serem inválidos ou nulos.");
        }
    }

    // Checa null/blank e se a URL de fato aponta para a plataforma esperada (ex.: "facebook" em uma URL de post)
    public static void validatePlatformUrl(String url, String platform){
        if (url == null || url.isBlank() || !url.toLowerCase().contains(platform.toLowerCase())) {
            log.warn("Requisição ignorada devido a URL inválida para {}: {}", platform, url);
            throw new InvalidURLException("A URL deve conter o endereço de algum objeto do " + platform + ".");
        }
    }

    // Checa null/blank para campos que não são URL (ex.: username/id de perfil)
    public static void requireNonBlank(String value, String fieldLabel){
        if (value == null || value.isBlank()) {
            log.warn("Requisição ignorada por campo obrigatório vazio: {}", fieldLabel);
            throw new InvalidURLException(fieldLabel + " não pode ser vazio.");
        }
    }
}
