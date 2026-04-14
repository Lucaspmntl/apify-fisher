package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.in.FisherIn;
import com.lucas.scraper.dto.in.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentScraperService {

    private final IgCommentsGateway commentsGateway;
    public CommentScraperService(IgCommentsGateway igCommentsGateway) {
        this.commentsGateway = igCommentsGateway;
    }
    public List<IgCommentsOut> getComments(IgCommentsIn input){
        return commentsGateway.getComments(input);
    }


    public List<IgCommentsOut> getInstagramFilteredComments(FisherIn input, String keyword){

        if (!input.instagramUrl().contains("instagram"))
            throw new RuntimeException("A URL deve conter o endereço de algum objeto do Instagram.");

        var raw = getComments(new IgCommentsIn(
                List.of(input.instagramUrl()),
                false,
                false,
                1000));

        return raw
                .stream()
                .filter(item -> item.text()
                        .toLowerCase()
                        .contains(keyword.toLowerCase()))
                .toList();
    }
}
