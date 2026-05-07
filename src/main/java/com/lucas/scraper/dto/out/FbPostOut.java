package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
) {
}
