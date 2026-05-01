package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record IgPostIn(
        String dataDetailLevel,
        int resultsLimit,
        boolean skipPinnedPosts,
        List<String> username
) {
}
