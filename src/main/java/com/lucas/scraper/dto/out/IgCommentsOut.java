package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgCommentsOut(
        String id,
        String text,
        String ownerUsername,
        String ownerProfilePicUrl,
        String timestamp,
        Integer likesCount,
        Integer repliesCount

        //List<Map<String, Object>> replies
) {
}
