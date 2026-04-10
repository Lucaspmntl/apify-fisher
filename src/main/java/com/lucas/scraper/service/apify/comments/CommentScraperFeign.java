package com.lucas.scraper.service.apify.comments;

import com.lucas.scraper.dto.request.ApifyCommentsRequestDTO;
import com.lucas.scraper.dto.response.ApifyCommentsResponseDTO;
import com.lucas.scraper.dto.startRun.startRunDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "apify", url = "https://api.apify.com/v2")
public interface CommentScraperFeign {

    @PostMapping(value = "/acts/{actorId}/run?waitForFinish=13", consumes = "application/json", produces = "application/json")
    public startRunDTO startRun(
            @PathVariable String actorId,
            @RequestBody ApifyCommentsRequestDTO dto,
            @RequestHeader("Authorization") String token);


    @GetMapping(value = "/actor-runs/{runId}", consumes = "application/json", produces = "application/json")
    public startRunDTO getRun(
            @PathVariable String runId,
            @RequestHeader("Authorization") String token
    );


    @GetMapping(value = "/datasets/{datasetId}/items", consumes = "application/json", produces = "application/json")
    public ApifyCommentsResponseDTO getDatasetItems(
            @PathVariable String datasetId,
            @RequestHeader("Authorization") String token
    );
}