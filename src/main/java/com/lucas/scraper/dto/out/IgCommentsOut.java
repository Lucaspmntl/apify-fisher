package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgCommentsOut(
        String id,
        String text,
        String ownerUsername,
        @JsonProperty("ownerProfilePicUrl") String profilePicUrl,
        @JsonProperty("timestamp") Date date,
        Integer likesCount,
        Integer repliesCount

        //List<Map<String, Object>> replies
) {
}
