package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.IgFisherDeltaCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsDeltaOut;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.IgPostOut;
import com.lucas.scraper.dto.out.IgProfileOut;
import com.lucas.scraper.service.InstagramService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fisher/api/v1/instagram")
public class InstagramController {

    private final InstagramService instagramService;
    private static final Logger log = LoggerFactory.getLogger(InstagramController.class);
    public InstagramController(InstagramService instagramService) {
        this.instagramService = instagramService;
    }


    @GetMapping("/comment")
    public ResponseEntity<List<IgCommentsOut>> getInstagramComments(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Instagram Controller: Recebida requisição de coleta de comentários em: {}", postUrl);
        List<IgCommentsOut> response = instagramService.getInstagramComments(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Instagram Controller: Coleta de comentários em {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/comment/delta")
    public ResponseEntity<IgCommentsDeltaOut> getInstagramDeltaComments(@RequestBody IgFisherDeltaCommentsIn data){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Instagram Comments Controller: Recebida requisição de coleta PARCIAL de comentários em: {}", data.postUrl());
        IgCommentsDeltaOut response = instagramService.getDeltaInstagramComments(data);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Instagram Comments Controller: Coleta de comentários parciais finalizada. Tempo: {}s", formatedSec);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile")
    public ResponseEntity<IgProfileOut> getInstagramProfile(@RequestParam String profile){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Instagram Controller: Recebida requisição de coleta de perfil: {}", profile);
        IgProfileOut response = instagramService.getInstagramProfile(profile);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Instagram Profile Controller: Coleta de perfil {} finalizada. Tempo: {}s", profile, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/post")
    public ResponseEntity<IgPostOut> getInstagramPost(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Instagram Controller: Recebida requisição de coleta de post: {}", postUrl);
        IgPostOut response = instagramService.getInstagramPost(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Instagram Post Controller: Coleta de post {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }
}
