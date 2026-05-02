package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.in.apify.FbPostIn;
import com.lucas.scraper.dto.in.apify.FbProfileIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.FbPostOut;
import com.lucas.scraper.dto.out.FbProfileOut;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.FbCommentsGateway;
import com.lucas.scraper.gateway.FbPostGateway;
import com.lucas.scraper.gateway.FbProfileGateway;
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

        if (!postUrl.toLowerCase().contains("facebook")) {
            log.warn("Facebook Service: Requisição ignorada devido invalidade de URL");
            throw new InvalidURLException("A URL deve conter o endereço de algum objeto do Instagram.");
        }

        log.info("Facebook Service: Iniciando coleta em: {}", postUrl);

        FbCommentsIn input = new FbCommentsIn(
                false,
                5,
                List.of(postUrl));
        List<FbCommentsOut> response = commentsGateway.getFacebookComments(input);

        log.info("Facebook Service: Foram coletados {} comentários em: {}", response.size(), postUrl);
        return response;
    }

    public List<FbPostOut> getFacebookPost(String postUrl){

        if (!postUrl.toLowerCase().contains("facebook")) {
            log.warn("Facebook Service: Requisição ignorada devido invalidade de URL");
            throw new InvalidURLException("A URL deve conter o endereço de algum objeto do Facebook.");
        }

        log.info("Facebook Service: Iniciando coleta do post: {}", postUrl);

        FbPostIn input = new FbPostIn(
                false,
                5,
                List.of(new FbPostIn.StartUrls(postUrl)));
        List<FbPostOut> response = postGateway.getFacebookPost(input);

        log.info("Facebook Service: Foram coletados {} posts para: {}", response.size(), postUrl);
        return response;
    }

    public List<FbProfileOut> getFacebookProfile(String profileId){

        String profileUrl = "https://www.facebook.com/" + profileId;

        log.info("Facebook Service: Iniciando coleta do perfil: {}", profileUrl);

        FbProfileIn input = new FbProfileIn(
                "details_by_url",
                0,
                profileUrl
        );
        List<FbProfileOut> response = profileGateway.getFacebookProfile(input);

        log.info("Facebook Service: Foram coletados {} perfis para: {}", response.size(), profileUrl);
        return response;
    }
}
