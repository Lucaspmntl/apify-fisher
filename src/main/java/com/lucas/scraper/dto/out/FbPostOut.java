package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbPostOut(
        @JsonAlias("postUrl") String postUrl,
        String title,
        String description,
        String id,
        String imageUrl,
        String ownerId,
        Integer reactionsCount,
        Integer commentsCount
) {
}
