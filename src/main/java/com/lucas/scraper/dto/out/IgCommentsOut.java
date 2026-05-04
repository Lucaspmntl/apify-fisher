package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgCommentsOut(
        String id,
        String text,
        String ownerUsername,
        @JsonAlias("ownerProfilePicUrl") String profilePicUrl,
        @JsonAlias("timestamp") OffsetDateTime date
        //Integer likesCount,
        //Integer repliesCount

        //List<Map<String, Object>> replies
) {
}
