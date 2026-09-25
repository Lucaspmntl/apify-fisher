package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.in.apify.FbPostIn;
import com.lucas.scraper.dto.in.apify.FbProfileIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.FbPostOut;
import com.lucas.scraper.dto.out.FbProfileOut;
import com.lucas.scraper.exception.InvalidResponseDataException;
import com.lucas.scraper.gateway.FbCommentsGateway;
import com.lucas.scraper.gateway.FbPostGateway;
import com.lucas.scraper.gateway.FbProfileGateway;
import com.lucas.scraper.utils.ValidationsUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacebookService {

    private final FbCommentsGateway commentsGateway;
    private final FbPostGateway postGateway;
    private final FbProfileGateway profileGateway;
    private static final Logger log = LoggerFactory.getLogger(FacebookService.class);

    public FacebookService(FbCommentsGateway fbCommentsGateway, FbPostGateway fbPostGateway, FbProfileGateway fbProfileGateway) {
        this.commentsGateway = fbCommentsGateway;
        this.postGateway = fbPostGateway;
        this.profileGateway = fbProfileGateway;
    }


    public List<FbCommentsOut> getFacebookComments(String postUrl){

        ValidationsUtils.validatePlatformUrl(postUrl, "Facebook");

        log.info("Facebook Service: Iniciando coleta em: {}", postUrl);

        FbCommentsIn input = new FbCommentsIn(
                false,
                5,
                List.of(new FbCommentsIn.StartUrls(postUrl)));
        List<FbCommentsOut> response = commentsGateway.getFacebookComments(input);

        ValidationsUtils.parseDataVality(response);

        log.info("Facebook Service: Foram coletados {} comentários em: {}", response.size(), postUrl);
        return response;
    }



    public FbPostOut getFacebookPost(String postUrl){

        ValidationsUtils.validatePlatformUrl(postUrl, "Facebook");

        log.info("Facebook Service: Iniciando coleta do post: {}", postUrl);


        FbPostIn input = new FbPostIn(
                false,
                5,
                List.of(new FbPostIn.StartUrls(postUrl)));
        List<FbPostOut> response = postGateway.getFacebookPost(input);

        ValidationsUtils.parseDataVality(response);

        log.info("Facebook Service: Foram coletados {} posts para: {}", response.size(), postUrl);
        return response.getFirst();
    }



    @SneakyThrows
    public FbProfileOut getFacebookProfile(String profileId){

        ValidationsUtils.requireNonBlank(profileId, "profileId");

        String profileUrl = "https://www.facebook.com/" + profileId;

        log.info("Facebook Service: Iniciando coleta do perfil: {}", profileUrl);

        FbProfileIn input = new FbProfileIn(
                "details_by_url",
                0,
                profileUrl
        );

        List<FbProfileOut> response = null;
        int attempts = 0;
        int maxAttempts = 5;
        boolean hasValidData = false;

        do {
            attempts++;
            response = profileGateway.getFacebookProfile(input);

            if (response != null && !response.isEmpty() && !response.getFirst().isBlankPayload()) {
                hasValidData = true;
                break;
            }

            log.warn("Facebook Service: Retorno vazio ou inválido na tentativa {} para {}. Tentando novamente...", attempts, profileUrl);
            Thread.sleep(3500);

        } while (attempts < maxAttempts);

        if (!hasValidData){
            log.error("Facebook Service: Dados vazios ou nulos, um erro externo impossibilitou coletar os dados de {}. \nTentativas totais: {}", profileUrl, attempts);
            throw new InvalidResponseDataException("Não foi possível coletar os dados do perfil após o limite de tentativas.");
        }

        log.info("Facebook Service: Foram coletados {} perfis para: {}", response.size(), profileUrl);
        return response.getFirst();
    }
}
