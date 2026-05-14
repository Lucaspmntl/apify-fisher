package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.lucas.scraper.utils.ValidatablePayload;

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

) implements ValidatablePayload {

    @Override
    public boolean isBlankPayload() {
        return (this.id == null || this.id.isBlank()) &&
                (this.text == null || this.text.isBlank()) &&
                this.date == null &&
                (this.ownerUsername == null || this.ownerUsername.isBlank()) &&
                (this.ownerId == null || this.ownerId.isBlank()) &&
                (this.ownerPicUrl == null || this.ownerPicUrl.isBlank());
    }
}