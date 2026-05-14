package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.in.apify.TtPostIn;
import com.lucas.scraper.dto.in.apify.TtProfileIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.TtCommentsGateway;
import com.lucas.scraper.gateway.TtPostGateway;
import com.lucas.scraper.gateway.TtProfileGateway;
import com.lucas.scraper.utils.ValidationsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiktokService {

    private final TtCommentsGateway commentsGateway;
    private final TtPostGateway postGateway;
    private final TtProfileGateway profileGateway;
    private static final Logger log = LoggerFactory.getLogger(TiktokService.class);
    public TiktokService(TtCommentsGateway ttCommentsGateway, TtPostGateway ttPostGateway, TtProfileGateway ttProfileGateway) {
        this.commentsGateway = ttCommentsGateway;
        this.postGateway = ttPostGateway;
        this.profileGateway = ttProfileGateway;
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

        ValidationsUtils.parseDataVality(response);

        log.info("Instagram Service: Foram coletados {} comentários em: {}", response.size(), postUrl);
        return response;
    }


    public TtPostOut getTiktokPost(String postUrl){

        if (!postUrl.toLowerCase().contains("tiktok")) {
            log.warn("Tiktok Service: Requisição ignorada devido invalidade de URL");
            throw new InvalidURLException("A URL deve conter o endereço de algum objeto do TikTok.");
        }

        log.info("TikTok Service: Iniciando coleta de post em: {}", postUrl);

        TtPostIn input = new TtPostIn(
                List.of(postUrl),
                100,
                false,
                false,
                false,
                false,
                "NEVER_DOWNLOAD_SUBTITLES");
        List<TtPostOut> response = postGateway.getPost(input);

        ValidationsUtils.parseDataVality(response);

        log.info("TikTok Service: Foram coletados {} posts em: {}", response.size(), postUrl);
        return response.getFirst();
    }


    public TtProfileOut getTikTokProfile(String profileId){

        log.info("TikTok Service: Iniciando coleta do perfil: {}", profileId);

        TtProfileIn input = new TtProfileIn(
                false,
                List.of(profileId),
                false,
                false,
                false,
                false,
                List.of("videos"),
                "latest",
                5,
                "NEVER_DOWNLOAD_SUBTITLES"
        );
        List<TtProfileOut> response = profileGateway.getProfile(input);

        ValidationsUtils.parseDataVality(response);

        log.info("TikTok Service: Foram coletados {} perfis para: {}", response.size(), profileId);
        return response.getFirst();
    }
}
