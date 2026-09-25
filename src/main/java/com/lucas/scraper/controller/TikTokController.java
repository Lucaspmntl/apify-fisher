package com.lucas.scraper.controller;

import com.lucas.scraper.dto.out.TtCommentsOut;
import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.service.TikTokService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TikTok")
@RestController
@RequestMapping("/fisher/api/v1/tiktok")
public class TikTokController {

    private final TikTokService tikTokService;
    private static final Logger log = LoggerFactory.getLogger(TikTokController.class);
    public TikTokController(TikTokService tikTokService) {
        this.tikTokService = tikTokService;
    }


    @GetMapping("/comment")
    public ResponseEntity<List<TtCommentsOut>> getTikTokComments(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("TikTok Controller: Recebida requisição de coleta de comentários em: {}", postUrl);
        List<TtCommentsOut> response = tikTokService.getTikTokComments(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("TikTok Controller: Coleta de comentários em {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/post")
    public ResponseEntity<TtPostOut> getTikTokPost(@RequestParam String postUrl){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("TikTok Controller: Recebida requisição de coleta de post em: {}", postUrl);
        TtPostOut response = tikTokService.getTikTokPost(postUrl);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("TikTok Controller: Coleta de post em {} finalizada. Tempo: {}s", postUrl, formatedSec);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/profile")
    public ResponseEntity<TtProfileOut> getTikTokProfile(@RequestParam String profile){

        StopWatch watch = new StopWatch();
        watch.start();

        log.info("TikTok Controller: Recebida requisição de coleta de perfil TikTok: {}", profile);
        TtProfileOut response = tikTokService.getTikTokProfile(profile);

        watch.stop();
        String formatedSec = String.format("%.2f", watch.getTotalTimeSeconds());

        log.info("TikTok Controller: Coleta de perfil {} finalizada. Tempo: {}s", profile, formatedSec);
        return ResponseEntity.ok(response);
    }
}
