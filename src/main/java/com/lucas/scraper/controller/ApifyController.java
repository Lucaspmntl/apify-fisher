package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.FisherIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.service.apify.comments.CommentScraperService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fisher")
public class ApifyController {

    private final CommentScraperService commentsService;
    public ApifyController(CommentScraperService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/comments")
    public ResponseEntity<List<IgCommentsOut>> getFilteredComments(@Valid @RequestBody FisherIn request){

        List<IgCommentsOut> response = commentsService
                .getInstagramFilteredComments(request, request.keyword());

        return ResponseEntity.ok(response);
    }
}
