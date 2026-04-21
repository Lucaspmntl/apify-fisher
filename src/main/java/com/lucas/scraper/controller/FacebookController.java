package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.FbFisherCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.service.FacebookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fisher/facebook")
public class FacebookController {

    private final FacebookService facebookService;
    private static final Logger log = LoggerFactory.getLogger(FacebookController.class);
    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }


    @PostMapping("/comments")
    public ResponseEntity<List<FbCommentsOut>> getFacebookComments(@Valid @RequestBody FbFisherCommentsIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Facebook Controller: Recebida requisição de coleta de comentários em: {}", input.postUrl());
        List<FbCommentsOut> response = facebookService.getFacebookComments(input.postUrl());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Facebook Controller: Coleta de comentários em {} finalizada. Tempo: {}s", input.postUrl(), formatedSec);
        return ResponseEntity.ok(response);
    }
}
