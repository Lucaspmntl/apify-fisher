package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TtPostOut(
        String id,
        @JsonAlias("text") String description,
        @JsonAlias("createTimeISO") OffsetDateTime date,
        @JsonAlias("commentCount") Integer commentsCount,
        @JsonAlias("diggCount") Integer likesCount,
        @JsonAlias("playCount") Integer viewsCount,
        @JsonAlias("webVideoUrl") String postUrl,
        String ownerId,
        @JsonAlias("coverUrl") String imageUrl
) {
}
