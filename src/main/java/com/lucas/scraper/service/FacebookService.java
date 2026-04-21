package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.gateway.FbCommentsGateway;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacebookService {

    private final FbCommentsGateway commentsGateway;
    public FacebookService(FbCommentsGateway fbCommentsGateway) {
        this.commentsGateway = fbCommentsGateway;
    }

    public List<FbCommentsOut> getFacebookComments(FbCommentsIn input){
        return commentsGateway.getComments(input);
    }
}
