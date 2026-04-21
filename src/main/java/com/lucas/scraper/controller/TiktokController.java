package com.lucas.scraper.controller;

import com.lucas.scraper.service.TiktokService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fisher/tiktok")
public class TiktokController {

    private final TiktokService tiktokService;
    public TiktokController(TiktokService tikTokService) {
        this.tiktokService = tikTokService;
    }
}
