package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.IgKeywordIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.service.apify.comments.CommentScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fisher")
public class    ApifyController {

    private CommentScraperService commentsService;

    public ApifyController(CommentScraperService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/comments")
    public ResponseEntity<List<IgCommentsOut>> getFilteredComments(@RequestBody IgKeywordIn request){
        List<IgCommentsOut> response = commentsService
                .getFilteredComments(request.apifyInput(), request.keyword());

        return ResponseEntity.ok(response);
    }
}
