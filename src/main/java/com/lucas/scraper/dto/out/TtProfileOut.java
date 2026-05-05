package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TtProfileOut(
        String id,
        String name,
        String profileUrl,
        @JsonAlias("nickName") String nickname,
        @JsonAlias("signature") String description,
        @JsonAlias("avatar") String imageUrl
) {
}
