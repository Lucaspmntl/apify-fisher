package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TtProfileOut(

        String id,
        @JsonAlias("name") String username,
        @JsonAlias("nickName") String fullName,
        @JsonAlias("signature") String biography,
        @JsonAlias("avatar") String profilePicUrl,
        String profileUrl,
        @JsonAlias("privateAccount") boolean isPrivate
) {
}
