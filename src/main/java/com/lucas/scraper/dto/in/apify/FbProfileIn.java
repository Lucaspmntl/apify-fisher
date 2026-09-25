package com.lucas.scraper.dto.in.apify;

import com.fasterxml.jackson.annotation.JsonProperty;

// endpoint/urlsText/maxPosts são os nomes de campo exigidos pelo actor do Apify (input schema
// snake_case) — @JsonProperty preserva o nome exato na serialização mantendo o campo Java idiomático
public record FbProfileIn(
        String endpoint,
        @JsonProperty("max_posts") int maxPosts,
        @JsonProperty("urls_text") String urlsText
) {
}
