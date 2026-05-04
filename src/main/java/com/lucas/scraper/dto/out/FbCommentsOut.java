package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbCommentsOut (
        String id,
        String feedbackId,
        Date date,
        String text,
        @JsonAlias("profilePicture") String profilePicUrl,
        String profileId,
        String likesCount,
        String facebookId,
        String inputUrl
){
}
