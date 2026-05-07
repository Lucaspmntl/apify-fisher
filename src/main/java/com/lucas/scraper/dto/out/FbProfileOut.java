package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbProfileOut(

        String id,
        @JsonAlias("name") String username,
        // fullName -> Não disponibilizado
        @JsonAlias("intro") String biography,
        @JsonAlias("image") String profilePicUrl,
        @JsonAlias("url") String profileUrl
) {
}
