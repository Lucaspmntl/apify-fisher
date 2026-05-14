package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lucas.scraper.utils.ValidatablePayload;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TtPostOut(

        // Post ---------------------------------------------------------------------------------
        String id,
        @JsonAlias("text") String description,
        @JsonAlias("createTimeISO") OffsetDateTime date,
        @JsonAlias("commentCount") Integer commentsCount,
        @JsonAlias("diggCount") Integer likesCount,
        @JsonAlias("webVideoUrl") String postUrl,
        @JsonAlias("coverUrl") String imageUrl,
        // videoUrl -> Não disponibilizado, o link do vídeo é o do post

        // Post owner ---------------------------------------------------------------------------
        String ownerId,
        String ownerUsername,
        String ownerFullName
) implements ValidatablePayload {

    @Override
    public boolean isBlankPayload() {
        return (this.id == null || this.id.isBlank()) &&
                (this.description == null || this.description.isBlank()) &&
                this.date == null &&
                this.commentsCount == null &&
                this.likesCount == null &&
                (this.postUrl == null || this.postUrl.isBlank()) &&
                (this.imageUrl == null || this.imageUrl.isBlank()) &&
                (this.ownerId == null || this.ownerId.isBlank()) &&
                (this.ownerUsername == null || this.ownerUsername.isBlank()) &&
                (this.ownerFullName == null || this.ownerFullName.isBlank());
    }
}
