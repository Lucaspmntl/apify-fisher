package com.lucas.scraper.controller;

import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.dto.out.FbPostOut;
import com.lucas.scraper.dto.out.FbProfileOut;

import com.lucas.scraper.service.FacebookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fisher/api/v1/facebook")
public class FacebookController {

    private final FacebookService facebookService;
    private static final Logger log = LoggerFactory.getLogger(FacebookController.class);
    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }


    @GetMapping("/comment")
    public ResponseEntity<List<FbCommentsOut>> getFacebookComments(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Facebook Controller: Recebida requisição de coleta de comentários em: {}", postUrl);
        List<FbCommentsOut> response = facebookService.getFacebookComments(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Facebook Controller: Coleta de comentários em {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/post")
    public ResponseEntity<FbPostOut> getFacebookPost(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Facebook Controller: Recebida requisição de coleta de post: {}", postUrl);
        FbPostOut response = facebookService.getFacebookPost(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Facebook Post Controller: Coleta de post {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile")
    public ResponseEntity<FbProfileOut> getFacebookProfile(@RequestParam String profile){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Facebook Controller: Recebida requisição de coleta de perfil: {}", profile);
        FbProfileOut response = facebookService.getFacebookProfile(profile);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Facebook Profile Controller: Coleta de perfil {} finalizada. Tempo: {}s", profile, formatedSec);
        return ResponseEntity.ok(response);
    }
}
