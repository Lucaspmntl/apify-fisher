package com.lucas.scraper.dto.in.apify;

import java.util.ArrayList;

public record FbCommentsIn(

        boolean includeNestedComments, // Always false
        int resultsLimit,
        ArrayList<String> startUrls
) {
}
