package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.TtCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.service.apify.ApifyGenericFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TtCommentsGateway {

    @Value("${apify.actor-id.tiktok-comment}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(TtCommentsGateway.class);
    private final ApifyGenericFeign apifyClient;
    public TtCommentsGateway(ApifyGenericFeign apifyClient) {
        this.apifyClient = apifyClient;
    }

    public List<TtCommentsOut> getComments(TtCommentsIn input) {
        return apifyClient.startRun(actorId, input, "Bearer " + token);
        // TODO: Fazer mapping de objetos genericos para TtCommentsOut
    }
}
