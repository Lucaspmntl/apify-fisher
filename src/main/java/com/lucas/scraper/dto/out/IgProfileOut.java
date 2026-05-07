package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgProfileOut(

        String id,
        String username,
        String fullName,
        String biography,
        @JsonAlias("profilePicUrlHD") String profilePicUrl,
        @JsonAlias("url") String profileUrl,
        @JsonAlias("private") boolean isPrivate
) {
}
