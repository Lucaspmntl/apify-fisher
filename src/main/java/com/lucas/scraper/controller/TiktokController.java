package com.lucas.scraper.controller;

import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.service.TiktokService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fisher/api/v1/tiktok")
public class TiktokController {

    private final TiktokService tiktokService;
    private static final Logger log = LoggerFactory.getLogger(TiktokController.class);
    public TiktokController(TiktokService tikTokService) {
        this.tiktokService = tikTokService;
    }


    @GetMapping("/comment")
    public ResponseEntity<List<TtCommentsOut>> getTiktokComments(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Tiktok Controller: Recebida requisição de coleta de comentários em: {}", postUrl);
        List<TtCommentsOut> response = tiktokService.getTiktokComments(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Tiktok Controller: Coleta de comentários em {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/post")
    public ResponseEntity<List<TtPostOut>> getTiktokPost(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Tiktok Controller: Recebida requisição de coleta de post em: {}", postUrl);
        List<TtPostOut> response = tiktokService.getTiktokPost(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Tiktok Controller: Coleta de post em {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile")
    public ResponseEntity<List<TtProfileOut>> getTikTokProfile(@RequestParam String profile){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("Tiktok Controller: Recebida requisição de coleta de perfil TikTok: {}", profile);
        List<TtProfileOut> response = tiktokService.getTikTokProfile(profile);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("Tiktok Controller: Coleta de perfil {} finalizada. Tempo: {}s", profile, formatedSec);
        return ResponseEntity.ok(response);
    }
}
