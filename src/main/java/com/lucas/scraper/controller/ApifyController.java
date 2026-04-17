package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.FisherIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.service.apify.comments.CommentScraperService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fisher")
public class ApifyController {
    private static final Logger log = LoggerFactory.getLogger(ApifyController.class);
    private final CommentScraperService commentsService;
    public ApifyController(CommentScraperService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/comments")
    public ResponseEntity<List<IgCommentsOut>> getFilteredComments(@Valid @RequestBody FisherIn request){
        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Controller: Recebida requisição de coleta para \"{}\"", request.keywords());
        List<IgCommentsOut> response = commentsService
                .getInstagramFilteredComments(request, request.keywords().toString());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Controller: Coleta de \"{}\" finalizada. Tempo: {}s", request.keywords(), formatedSec);
        return ResponseEntity.ok(response);
    }
}
