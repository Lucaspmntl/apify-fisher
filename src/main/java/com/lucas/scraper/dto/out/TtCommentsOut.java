package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record TtCommentsOut(
        String text,
        @JsonProperty("createTimeISO") Date date,
        @JsonProperty("uniqueId") String id,
        String uid,
        String cid,
        @JsonProperty("avatarThumbnail") String profilePicUrl
) {}