package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.OffsetDateTime;

public record FbCommentsOut (

        // Comment
        @JsonAlias("commentId") String id,
        String text,
        OffsetDateTime date,

        // Comment Owner
        @JsonAlias("profileName") String ownerUsername,
        @JsonAlias("profileId") String ownerId,
        @JsonAlias("profilePicture") String ownerPicUrl

){
}