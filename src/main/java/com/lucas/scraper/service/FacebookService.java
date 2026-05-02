package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.in.apify.FbPostIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.FbPostOut;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.FbCommentsGateway;
import com.lucas.scraper.gateway.FbPostGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacebookService {

    private final FbCommentsGateway commentsGateway;
    private final FbPostGateway postGateway;
    private static final Logger log = LoggerFactory.getLogger(FacebookService.class);
    public FacebookService(FbCommentsGateway fbCommentsGateway, FbPostGateway fbPostGateway) {
        this.commentsGateway = fbCommentsGateway;
        this.postGateway = fbPostGateway;
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
}
