package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record FbCommentsIn(

        boolean includeNestedComments, // Always false
        int resultsLimit,
        List<StartUrls> startUrls
) {
    public record StartUrls(String url) {}
}
