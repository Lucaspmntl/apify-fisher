package com.lucas.scraper.dto.response.startRun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Data(

        @JsonProperty("id")
        String runId,

        String actId,
        String userId,
        String actorTaskId,
        String startedAt,
        String finishedAt,
        String status,
        String statusMessage
){
}