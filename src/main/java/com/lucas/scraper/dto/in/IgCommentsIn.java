package com.lucas.scraper.dto.in;

import java.util.List;

public record IgCommentsIn(

        List<String> directUrls,
        boolean includeNestedComments, // Always false
        boolean includeReplies,        // Always false
        int resultsLimit
) {
}
