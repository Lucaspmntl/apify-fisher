package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
) {
}
