package com.lucas.scraper.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApifyCommentsResponseDTO(
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
