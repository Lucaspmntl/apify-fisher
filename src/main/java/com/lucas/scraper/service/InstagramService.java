package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.IgFisherDeltaCommentsIn;
import com.lucas.scraper.dto.in.apify.IgCommentsIn;
import com.lucas.scraper.dto.in.apify.IgPostIn;
import com.lucas.scraper.dto.in.apify.IgProfileIn;
import com.lucas.scraper.dto.out.IgCommentsDeltaOut;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.IgPostOut;
import com.lucas.scraper.dto.out.IgProfileOut;
import com.lucas.scraper.gateway.IgCommentsGateway;
import com.lucas.scraper.gateway.IgPostGateway;
import com.lucas.scraper.gateway.IgProfileGateway;
import com.lucas.scraper.utils.ValidationsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstagramService {

    private static final Logger log = LoggerFactory.getLogger(InstagramService.class);
    private final IgCommentsGateway commentsGateway;
    private final IgProfileGateway profileGateway;
    private final IgPostGateway postGateway;

    public InstagramService(IgCommentsGateway igCommentsGateway, IgProfileGateway profileGateway, IgPostGateway postGateway) {
        this.commentsGateway = igCommentsGateway;
        this.profileGateway = profileGateway;
        this.postGateway = postGateway;
    }



    public List<IgCommentsOut> getInstagramComments(String postUrl){

        ValidationsUtils.validatePlatformUrl(postUrl, "Instagram");

        log.info("Instagram Service: Iniciando coleta em: {}", postUrl);

        IgCommentsIn input = new IgCommentsIn(
                List.of(postUrl),
                false,
                false,
                1000);
        List<IgCommentsOut> response = commentsGateway.getInstagramComments(input);

        ValidationsUtils.parseDataVality(response);

        log.info("Instagram Service: Foram coletados {} comentários em: {}", response.size(), postUrl);
        return response;
    }



    public IgCommentsDeltaOut getDeltaInstagramComments(IgFisherDeltaCommentsIn data){

        ValidationsUtils.validatePlatformUrl(data.postUrl(), "Instagram");

        log.info("Instagram Service: Iniciando coleta parcial para comentários de ID's {}", data.lastCommentsIds().toString());

        IgCommentsDeltaOut response = commentsGateway.getDeltaInstagramComments(data);

        ValidationsUtils.parseDataVality(response.comments());

        log.info("Instagram Profile Service: Foram coletados {} comentários parciais para: {}", response.comments().size(), data.postUrl());
        return response;
    }



    public IgProfileOut getInstagramProfile(String username){

        ValidationsUtils.requireNonBlank(username, "username");

        log.info("Instagram Profile Service: Iniciando coleta do perfil: {}", username);

        IgProfileIn input = new IgProfileIn(
                false,
                List.of(username)
        );
        List<IgProfileOut> response = profileGateway.getInstagramProfile(input);

        ValidationsUtils.parseDataVality(response);

        log.info("Instagram Profile Service: Foram coletados {} perfis para: {}", response.size(), username);
        return response.getFirst();
    }



    public IgPostOut getInstagramPost(String postUrl){

        ValidationsUtils.validatePlatformUrl(postUrl, "Instagram");

        log.info("Instagram Service: Iniciando coleta do post: {}", postUrl);

        IgPostIn input = new IgPostIn(
                "basicData",
                24,
                false,
                List.of(postUrl)
        );

        List<IgPostOut> response = postGateway.getInstagramPost(input);

        ValidationsUtils.parseDataVality(response);

        log.info("Instagram Service: Foram coletados {} posts para: {}", response.size(), postUrl);
        return response.getFirst();
    }
}
