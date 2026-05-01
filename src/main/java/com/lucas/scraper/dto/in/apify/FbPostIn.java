package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record FbPostIn(
        boolean captionText,
        int resultsLimit,
        List<String> startUrls
) {
}
