package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record TtCommentsIn(
        int commentsPerPost,
        boolean excludePinnedPosts, // Always false
        int maxRepliesPerComment, // Always 0
        List<String> postURLs,
        int resultsPerPage
) {
}