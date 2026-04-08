package com.lucas.scraper.service.apify;

import com.lucas.scraper.dto.startRun.startRunDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "apify", url = "https://api.apify.com/v2")
public interface ApifyActorService {

    @PostMapping(value = "/acts/{actorId}/run?waitForFinish=13", consumes = "application/json", produces = "application/json")
    public startRunDTO startRun(
            @PathVariable String actorId,
            @RequestHeader("Authorization") String token);


    @GetMapping(value = "/v2/actor-runs/{runId}", consumes = "application/json", produces = "application/json")
    public startRunDTO getRun(
            @PathVariable String runId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping(value = "/v2/datasets/{datasetId}/items", consumes = "application/json", produces = "application/json")
    public Object getDatasetItems(
            @PathVariable String datasetId,
            @RequestHeader("Authorization") String token
    );
}