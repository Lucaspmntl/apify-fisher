package com.lucas.scraper.dto.out.startRun;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RunResponseOut(
        Data data
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Data(

            @JsonAlias("id") String runId,
            String actId,
            String userId,
            String actorTaskId,
            String startedAt,
            String finishedAt,
            String status,
            String statusMessage
    ){
    }
}
