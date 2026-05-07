package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RunOut(
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
