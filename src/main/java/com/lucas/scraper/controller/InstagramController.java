package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.IgFisherCommentsIn;
import com.lucas.scraper.dto.in.IgFisherProfileIn;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.IgProfileOut;
import com.lucas.scraper.service.InstagramService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fisher/instagram")
public class InstagramController {

    private final InstagramService instagramService;
    private static final Logger log = LoggerFactory.getLogger(InstagramController.class);
    public InstagramController(InstagramService instagramService) {
        this.instagramService = instagramService;
    }


    @PostMapping("/comments")
    public ResponseEntity<List<IgCommentsOut>> getInstagramComments(@Valid @RequestBody IgFisherCommentsIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Instagram Controller: Recebida requisição de coleta de comentários em: {}", input.postUrl());
        List<IgCommentsOut> response = instagramService.getInstagramComments(input.postUrl());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Instagram Controller: Coleta de comentários em {} finalizada. Tempo: {}s", input.postUrl(), formatedSec);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<List<IgProfileOut>> getInstagramProfile(@Valid @RequestBody IgFisherProfileIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Instagram Profile Controller: Recebida requisição de coleta de perfil: {}", input.username());
        List<IgProfileOut> response = instagramService.getInstagramProfile(input.username());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Instagram Profile Controller: Coleta de perfil {} finalizada. Tempo: {}s", input.username(), formatedSec);
        return ResponseEntity.ok(response);
    }
}
