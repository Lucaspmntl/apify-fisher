package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.FbFisherCommentsIn;
import com.lucas.scraper.dto.in.FbFisherPostIn;
import com.lucas.scraper.dto.in.FbFisherProfileIn;
import com.lucas.scraper.dto.in.TtFisherProfileIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.FbPostOut;
import com.lucas.scraper.dto.out.FbProfileOut;
import com.lucas.scraper.dto.out.TtProfileOut;
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


    @PostMapping("/comment")
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

    @PostMapping("/post")
    public ResponseEntity<List<FbPostOut>> getFacebookPost(@Valid @RequestBody FbFisherPostIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Facebook Controller: Recebida requisição de coleta de post: {}", input.postUrl());
        List<FbPostOut> response = facebookService.getFacebookPost(input.postUrl());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Facebook Post Controller: Coleta de post {} finalizada. Tempo: {}s", input.postUrl(), formatedSec);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<List<FbProfileOut>> getFacebookProfile(@Valid @RequestBody FbFisherProfileIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Facebook Controller: Recebida requisição de coleta de perfil: {}", input.profileId());
        List<FbProfileOut> response = facebookService.getFacebookProfile(input.profileId());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Facebook Profile Controller: Coleta de perfil {} finalizada. Tempo: {}s", input.profileId(), formatedSec);
        return ResponseEntity.ok(response);
    }
}
