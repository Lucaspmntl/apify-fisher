package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgPostOut(
        @JsonAlias("caption") String description,
        String ownerFullName,
        String ownerUsername,
        @JsonAlias("url") String postUrl,
        int commentsCount,
        int likesCount,
        @JsonAlias("displayUrl") String imageUrl,
        String videoUrl,
        OffsetDateTime timestamp
) {
}
