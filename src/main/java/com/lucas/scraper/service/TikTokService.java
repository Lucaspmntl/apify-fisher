package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.in.apify.TtPostIn;
import com.lucas.scraper.dto.in.apify.TtProfileIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.gateway.TtCommentsGateway;
import com.lucas.scraper.gateway.TtPostGateway;
import com.lucas.scraper.gateway.TtProfileGateway;
import com.lucas.scraper.utils.ValidationsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TikTokService {

    private final TtCommentsGateway commentsGateway;
    private final TtPostGateway postGateway;
    private final TtProfileGateway profileGateway;
    private static final Logger log = LoggerFactory.getLogger(TikTokService.class);
    public TikTokService(TtCommentsGateway ttCommentsGateway, TtPostGateway ttPostGateway, TtProfileGateway ttProfileGateway) {
        this.commentsGateway = ttCommentsGateway;
        this.postGateway = ttPostGateway;
        this.profileGateway = ttProfileGateway;
    }

    public List<TtCommentsOut> getTikTokComments(String postUrl){

        ValidationsUtils.validatePlatformUrl(postUrl, "TikTok");

        log.info("TikTok Service: Iniciando coleta em: {}", postUrl);

        TtCommentsIn input = new TtCommentsIn(
                1000,
                false,
                0,
                List.of(postUrl),
                1000);
        List<TtCommentsOut> response = commentsGateway.getComments(input);

        ValidationsUtils.parseDataVality(response);

        log.info("TikTok Service: Foram coletados {} comentários em: {}", response.size(), postUrl);
        return response;
    }


    public TtPostOut getTikTokPost(String postUrl){

        ValidationsUtils.validatePlatformUrl(postUrl, "TikTok");

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

        ValidationsUtils.requireNonBlank(profileId, "profileId");

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
