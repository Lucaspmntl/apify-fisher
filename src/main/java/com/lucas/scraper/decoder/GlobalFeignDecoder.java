package com.lucas.scraper.decoder;

import com.lucas.scraper.exception.apify.*;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GlobalFeignDecoder implements ErrorDecoder {

    private static final Logger log = LoggerFactory.getLogger(GlobalFeignDecoder.class);

    @Override
    public Exception decode(String method, Response response) {

        log.error("Erro na chamada Apify. Método:{}, Status: {}.", method, response.status());

        return switch (response.status()){
            case 400 -> new InvalidInputException(
                    "Entrada inválida: O corpo da requisição contém dados inválidos."
            );
            case 401 -> new InvalidTokenException(
                    "O Token de autenticação não é válido."
            );
            case 403 -> new InsufficientPermissionsException(
                    "Você não tem permissão suficente para realizar esta ação."
            );
            case 404 -> new ResourceNotFoundException(
                    "O recurso solicitado não foi encontrado."
            );
            case 405 -> new MethodNotAllowedException(
                    "O método HTTP utilizado não é permitido para esta requisição."
            );
            case 429 -> new RateLimitExceedException(
                    "O limite de tentativas foi excedido. Tente novamente mais tarde."
            );
            default -> new DefaultIntegrationException(
                    "Erro de integração não tratado.", response.status()
            );
        };
    }
}
