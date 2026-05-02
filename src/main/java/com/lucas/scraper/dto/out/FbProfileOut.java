package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbProfileOut(
        String name,
        String id,
        String url,
        @JsonProperty("profilePicUrl") String profilePicUrl,
        String biography
) {
}
