package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.request.ApifyCommentsRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommentScraperService {

    @Value("${apify.actor-id.comment}")
    String CommentActorId;

    private CommentScraperFeign commentScraperFeign;

    public CommentScraperService(CommentScraperFeign commentScraperFeign) {}
    

}
