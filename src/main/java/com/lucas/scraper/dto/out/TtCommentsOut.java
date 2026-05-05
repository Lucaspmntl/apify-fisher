package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Date;

public record TtCommentsOut(
        String text,
        @JsonAlias("createTimeISO") OffsetDateTime date,
        @JsonAlias("uniqueId") String id,
        String uid,
        String cid,
        @JsonProperty("avatarThumbnail") String profilePicUrl
) {}