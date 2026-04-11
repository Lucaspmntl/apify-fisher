package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.request.ApifyCommentsRequestDTO;
import com.lucas.scraper.dto.response.ApifyCommentsResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentScraperService {

    private CommentsScraperGateway commentsGateway;

    public CommentScraperService(CommentsScraperGateway commentsGateway) {
        this.commentsGateway = commentsGateway;
    }

    public List<ApifyCommentsResponseDTO> getComments(ApifyCommentsRequestDTO request){
        return commentsGateway.getComments(request);
    }

}
