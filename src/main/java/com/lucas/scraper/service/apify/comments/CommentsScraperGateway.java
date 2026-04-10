package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.request.ApifyCommentsRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentsScraperGateway {

    private CommentScraperFeign commentScraperFeign;

    public CommentsScraperGateway(CommentScraperFeign commentScraperFeign) {
        this.commentScraperFeign = commentScraperFeign;
    }

    public ApifyCommentsRequestDTO getComments(ApifyCommentsRequestDTO dto) {
        return null;
    }
}
