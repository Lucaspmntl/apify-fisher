package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.lucas.scraper.utils.ValidatablePayload;

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
