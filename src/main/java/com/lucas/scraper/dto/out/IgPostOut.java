package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lucas.scraper.utils.ValidatablePayload;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IgPostOut(

        // Post ---------------------------------------------------------------------------------
        String id,
        @JsonAlias("caption") String description,
        @JsonAlias("timestamp") OffsetDateTime date,
        Integer commentsCount,
        Integer likesCount,
        @JsonAlias("url") String postUrl,
        @JsonAlias("displayUrl") String imageUrl,
        String videoUrl,

        // Post owner ---------------------------------------------------------------------------
        String ownerFullName,
        String ownerUsername,
        String ownerId
) implements ValidatablePayload {
    public IgPostOut {

        // Em casos de posts em que o like só pode ser visualizado pelo owner o valor retornado é -1
        if (likesCount != null && likesCount < 0)
            likesCount = null;

        // Em casos de posts em que o comentário só pode ser visualizado pelo owner o valor retornado é -1
        if (commentsCount != null && commentsCount < 0)
            commentsCount = null;

        // Em casos de posts que não são vídeos
        if (videoUrl == null)
            videoUrl = imageUrl;
    }

    @Override
    public boolean isBlankPayload() {
        return (this.id == null || this.id.isBlank()) &&
                (this.description == null || this.description.isBlank()) &&
                this.date == null &&
                this.commentsCount == null &&
                this.likesCount == null &&
                (this.postUrl == null || this.postUrl.isBlank()) &&
                (this.imageUrl == null || this.imageUrl.isBlank()) &&
                (this.videoUrl == null || this.videoUrl.isBlank()) &&
                (this.ownerFullName == null || this.ownerFullName.isBlank()) &&
                (this.ownerUsername == null || this.ownerUsername.isBlank()) &&
                (this.ownerId == null || this.ownerId.isBlank());
    }
}
