package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
) {
    public IgPostOut {

        // Em casos de posts em que o like só pode ser visualizado pelo owner o valor retornado é -1
        if(likesCount < 0)
            likesCount = null;

        // Em casos de posts em que o comentário só pode ser visualizado pelo owner o valor retornado é -1
        if (commentsCount < 0)
            commentsCount = null;

        // Em casos de posts que não são vídeos
        if (videoUrl == null)
            videoUrl = imageUrl;
    }
}
