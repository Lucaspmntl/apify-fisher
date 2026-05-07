package com.lucas.scraper.utils;

import com.lucas.scraper.dto.out.RunOut;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "apify", url = "https://api.apify.com/v2")
public interface ApifyGenericFeign {

    @PostMapping(value = "/acts/{actorId}/runs?waitForFinish=5", consumes = "application/json", produces = "application/json")
    public RunOut startRun(
            @PathVariable String actorId,
            @RequestBody Object input,
            @RequestHeader("Authorization") String token);


    @GetMapping(value = "/actor-runs/{runId}", consumes = "application/json", produces = "application/json")
    public RunOut getRunDetails(
            @PathVariable String runId,
            @RequestHeader("Authorization") String token
    );


    @GetMapping(value = "/actor-runs/{runId}/dataset/items", consumes = "application/json", produces = "application/json")
    public List<Map<String, Object>> getDatasetItems(
            @PathVariable String runId,
            @RequestHeader("Authorization") String token
    );
}
