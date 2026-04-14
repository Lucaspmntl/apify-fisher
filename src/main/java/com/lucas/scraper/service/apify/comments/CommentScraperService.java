package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.IgCommentsInput;
import com.lucas.scraper.dto.out.IgCommentsOut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentScraperService {

    private final IgCommentsGateway commentsGateway;

    public CommentScraperService(IgCommentsGateway igCommentsGateway) {
        this.commentsGateway = igCommentsGateway;
    }

    public List<IgCommentsOut> getComments(IgCommentsInput input){
        return commentsGateway.getComments(input);
    }

    public List<IgCommentsOut> getFilteredComments(IgCommentsInput input, String keyword){

        var raw = getComments(input);

        return raw
                .stream()
                .filter(item -> item.text()
                        .toLowerCase()
                        .contains(keyword.toLowerCase()))
                .toList();
    }
}
