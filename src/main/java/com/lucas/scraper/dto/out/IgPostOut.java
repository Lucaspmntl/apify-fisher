package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgPostOut(
        @JsonProperty("caption") String description,
        String ownerFullName,
        String ownerUsername,
        String url,
        int commentsCount,
        int likesCount,
        OffsetDateTime timestamp
) {
}
