package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.gateway.TtCommentsGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiktokService {

    private final TtCommentsGateway commentsGateway;
    public TiktokService(TtCommentsGateway ttCommentsGateway) {
        this.commentsGateway = ttCommentsGateway;
    }

    public List<TtCommentsOut> getTiktokComments(String postUrl){
        TtCommentsIn input = new TtCommentsIn(
                1000,
                false,
                0,
                List.of(postUrl),
                1000);

        return commentsGateway.getComments(input);
    }
}
