package com.lucas.scraper.dto.in.apify;

public record FbProfileIn(
        String endpoint,
        int max_posts,
        String urls_text
) {
}
