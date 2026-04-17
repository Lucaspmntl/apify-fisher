package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.FbCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.service.apify.ApifyGenericFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FbCommentsGateway {

    @Value("${apify.actor-id.facebook-comment}")
    String actorId;

    @Value("${apify.token}")
    String token;

    private static final Logger log = LoggerFactory.getLogger(FbCommentsGateway.class);
    private final ApifyGenericFeign apifyClient;
    public FbCommentsGateway(ApifyGenericFeign apifyClient) {
        this.apifyClient = apifyClient;
    }

    public FbCommentsOut getComments(FbCommentsIn input) {
        return apifyClient.startRun(actorId, input, "Bearer " + token);
        // TODO: Fazer mapping de objetos genericos para FbCommentsOut
    }
}
