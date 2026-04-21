package com.lucas.scraper.controller;

import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.service.InstagramService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fisher/instagram")
public class InstagramController {
    private static final Logger log = LoggerFactory.getLogger(InstagramController.class);
    private final InstagramService commentsService;
    public InstagramController(InstagramService commentsService) {
        this.commentsService = commentsService;
    }


    @PostMapping("/comments/{postUrl}")
    public ResponseEntity<List<IgCommentsOut>> getComments(@PathVariable String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Comments Controller: Recebida requisição de coleta para \"{}\"", postUrl);
        List<IgCommentsOut> response = commentsService.getInstagramComments(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Comments Controller: Coleta de \"{}\" finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }
}
