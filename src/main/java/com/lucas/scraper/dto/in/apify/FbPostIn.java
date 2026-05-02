package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record FbPostIn(
        boolean captionText,
        int resultsLimit,
        List<StartUrls> startUrls
) {

    public static record StartUrls(
            String url
    ) {
    }
}