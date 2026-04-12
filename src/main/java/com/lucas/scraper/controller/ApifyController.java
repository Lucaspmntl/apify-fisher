package com.lucas.scraper.controller;

import com.lucas.scraper.dto.request.ApifyFilterCommentsRequestDTO;
import com.lucas.scraper.dto.response.ApifyCommentsResponseDTO;
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
    public ResponseEntity<List<ApifyCommentsResponseDTO>> getFilteredComments(@RequestBody ApifyFilterCommentsRequestDTO request){
        List<ApifyCommentsResponseDTO> response = commentsService
                .getFilteredComments(request.apifyInput(), request.stringFilter());

        return ResponseEntity.ok(response);
    }
}
