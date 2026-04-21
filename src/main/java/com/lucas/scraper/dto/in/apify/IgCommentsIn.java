package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record IgCommentsIn(

        List<String> directUrls,
        boolean includeNestedComments, // Always false
        boolean includeReplies,        // Always false
        int resultsLimit
) {
}
