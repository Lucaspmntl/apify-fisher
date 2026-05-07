package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.OffsetDateTime;

public record IgCommentsOut(

        // Comment
        String id,
        String text,
        @JsonAlias("timestamp") OffsetDateTime date,

        // Comment owner
        String ownerUsername,
        String ownerId,
        @JsonAlias("ownerProfilePicUrl") String ownerPicUrl

) {
}
