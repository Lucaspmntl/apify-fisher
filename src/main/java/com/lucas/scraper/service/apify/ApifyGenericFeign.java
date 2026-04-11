package com.lucas.scraper.service.apify;

import com.lucas.scraper.dto.response.startRun.ApifyRunResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "apify", url = "https://api.apify.com/v2")
public interface ApifyGenericFeign {

    // Verificar possibilidade de ser /run ao invés de /runs
    @PostMapping(value = "/acts/{actorId}/runs?waitForFinish=13", consumes = "application/json", produces = "application/json")
    public ApifyRunResponse startRun(
            @PathVariable String actorId,
            @RequestBody Object input,
            @RequestHeader("Authorization") String token);


    @GetMapping(value = "/actor-runs/{runId}", consumes = "application/json", produces = "application/json")
    public ApifyRunResponse getRunDetails(
            @PathVariable String runId,
            @RequestHeader("Authorization") String token
    );


    @GetMapping(value = "/actor-runs/{runId}/dataset", consumes = "application/json", produces = "application/json")
    public List<Map<String, Object>> getDatasetItems(
            @PathVariable String runId,
            @RequestHeader("Authorization") String token
    );
}
