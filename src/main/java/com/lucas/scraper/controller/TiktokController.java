package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.TtFisherCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.service.TiktokService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fisher/tiktok")
public class TiktokController {

    private final TiktokService tiktokService;
    private static final Logger log = LoggerFactory.getLogger(TiktokController.class);
    public TiktokController(TiktokService tikTokService) {
        this.tiktokService = tikTokService;
    }


    @PostMapping("/comments")
    public ResponseEntity<List<TtCommentsOut>> getTiktokComments(@Valid @RequestBody TtFisherCommentsIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Tiktok Controller: Recebida requisição de coleta de comentários em: {}", input.postUrl());
        List<TtCommentsOut> response = tiktokService.getTiktokComments(input.postUrl());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Tiktok Controller: Coleta de comentários em {} finalizada. Tempo: {}s", input.postUrl(), formatedSec);
        return ResponseEntity.ok(response);
    }
}
