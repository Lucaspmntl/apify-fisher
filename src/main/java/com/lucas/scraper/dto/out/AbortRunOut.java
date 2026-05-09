package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AbortRunOut(
    data data
)
{

    public record data(
      String id,
      String actId,
      OffsetDateTime startedAt,
      OffsetDateTime finishedAt,
      String status
    ){
    }

    public record storageIds(
        datasets datasets
    )
    {
            public record datasets(
                @JsonProperty("default") String id
            ){}
    }
}
