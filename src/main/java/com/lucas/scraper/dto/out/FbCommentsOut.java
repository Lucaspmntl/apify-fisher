package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbCommentsOut (
        @JsonAlias("commentId") String id,
        OffsetDateTime date,
        String text,
        @JsonAlias("profilePicture") String profilePicUrl,
        String profileId,
        String likesCount
){
}
