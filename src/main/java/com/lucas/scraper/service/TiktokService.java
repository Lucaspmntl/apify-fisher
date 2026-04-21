package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.TtCommentsGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiktokService {

    private final TtCommentsGateway commentsGateway;
    private static final Logger log = LoggerFactory.getLogger(TiktokService.class);
    public TiktokService(TtCommentsGateway ttCommentsGateway) {
        this.commentsGateway = ttCommentsGateway;
    }

    public List<TtCommentsOut> getTiktokComments(String postUrl){

        if (!postUrl.toLowerCase().contains("tiktok")) {
            log.warn("Tiktok Service: Requisição ignorada devido invalidade de URL");
            throw new InvalidURLException("A URL deve conter o endereço de algum objeto do Instagram.");
        }

        log.info("TikTok Service: Iniciando coleta em: {}", postUrl);

        TtCommentsIn input = new TtCommentsIn(
                1000,
                false,
                0,
                List.of(postUrl),
                1000);
        List<TtCommentsOut> response = commentsGateway.getComments(input);

        log.info("Instagram Service: Foram coletados {} comentários em: {}", response.size(), postUrl);
        return response;
    }
}
