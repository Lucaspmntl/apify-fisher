package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgProfileOut(
        String id,
        String username,
        String biography,
        @JsonProperty("profilePicUrl") String profilePicUrl,
        @JsonProperty("profilePicUrlHD") String profilePicUrlHD,
        boolean privateAccount,
        String fullName,
        String url
) {
}
