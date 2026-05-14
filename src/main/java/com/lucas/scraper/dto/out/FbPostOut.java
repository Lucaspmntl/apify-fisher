package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lucas.scraper.utils.ValidatablePayload;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FbPostOut(

        // Post ---------------------------------------------------------------------------------
        String id,
        @JsonAlias("previewDescription") String description,
        @JsonAlias("publish_time") OffsetDateTime date,
        Integer commentsCount,
        Integer reactionsCount,
        String imageUrl,
        @JsonAlias("facebookUrl") String postUrl,
        // videoUrl -> Dado não considerado pois é aninhado dentro de xml e de expiração rápida


        // Post Owner ----------------------------------------------------------------------------
        // OwnerFullname -> Dado não disponibilizado
        String ownerUsername, // Dado não disponibilizado, mas extraído da URL do post
        String ownerId
) implements ValidatablePayload {

    @Override
    public boolean isBlankPayload() {
        return (this.id == null || this.id.isBlank()) &&
                (this.description == null || this.description.isBlank()) &&
                this.date == null &&
                this.commentsCount == null &&
                this.reactionsCount == null &&
                (this.imageUrl == null || this.imageUrl.isBlank()) &&
                (this.postUrl == null || this.postUrl.isBlank()) &&
                (this.ownerUsername == null || this.ownerUsername.isBlank()) &&
                (this.ownerId == null || this.ownerId.isBlank());
    }
}
