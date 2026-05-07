package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.OffsetDateTime;


public record TtCommentsOut(

        // Comment
        @JsonAlias("cid") String id,
        String text,
        @JsonAlias("createTimeISO") OffsetDateTime date,

        // Comment owner
        @JsonAlias("uniqueId") String ownerUsername,
        @JsonAlias("uid") String ownerId,
        @JsonAlias("avatarThumbnail") String ownerPicUrl

) {}