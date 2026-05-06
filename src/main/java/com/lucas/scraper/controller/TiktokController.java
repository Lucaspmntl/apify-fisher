package com.lucas.scraper.controller;

import com.lucas.scraper.dto.in.TtFisherCommentsIn;
import com.lucas.scraper.dto.in.TtFisherPostIn;
import com.lucas.scraper.dto.in.TtFisherProfileIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.dto.out.TtProfileOut;
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
@RequestMapping("/fisher/api/v1/tiktok")
public class TiktokController {

    private final TiktokService tiktokService;
    private static final Logger log = LoggerFactory.getLogger(TiktokController.class);
    public TiktokController(TiktokService tikTokService) {
        this.tiktokService = tikTokService;
    }


    @PostMapping("/comment")
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

    @PostMapping("/post")
    public ResponseEntity<List<TtPostOut>> getTiktokPost(@Valid @RequestBody TtFisherPostIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Tiktok Controller: Recebida requisição de coleta de post em: {}", input.postUrl());
        List<TtPostOut> response = tiktokService.getTiktokPost(input.postUrl());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Tiktok Controller: Coleta de post em {} finalizada. Tempo: {}s", input.postUrl(), formatedSec);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile")
    public ResponseEntity<List<TtProfileOut>> getTikTokProfile(@Valid @RequestBody TtFisherProfileIn input){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Tiktok Controller: Recebida requisição de coleta de perfil TikTok: {}", input.profileId());
        List<TtProfileOut> response = tiktokService.getTikTokProfile(input.profileId());

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Tiktok Controller: Coleta de perfil {} finalizada. Tempo: {}s", input.profileId(), formatedSec);
        return ResponseEntity.ok(response);
    }
}
