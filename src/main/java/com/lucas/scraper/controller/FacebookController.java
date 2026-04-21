package com.lucas.scraper.controller;

import com.lucas.scraper.service.FacebookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fisher/facebook")
public class FacebookController {

    private final FacebookService facebookService;
    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }
}
