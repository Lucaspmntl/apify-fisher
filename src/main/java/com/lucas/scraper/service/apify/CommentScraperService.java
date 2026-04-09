package com.lucas.scraper.service.apify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentScraperService {

    @Autowired
    private ApifyActorService apifyActorService;

    public void scrapComments() {
    }
}
