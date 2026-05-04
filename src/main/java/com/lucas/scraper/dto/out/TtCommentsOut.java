package com.lucas.scraper.dto.out;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public record TtCommentsOut(
        String text,
        @JsonAlias("createTimeISO") Date date,
        @JsonAlias("uniqueId") String id,
        String uid,
        String cid,
        @JsonProperty("avatarThumbnail") String profilePicUrl
) {}