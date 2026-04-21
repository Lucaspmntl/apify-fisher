package com.lucas.scraper.dto.in;

import java.util.ArrayList;

public record TtCommentsIn(
        int commentsPerPost,
        boolean excludePinnedPosts, // Always false
        int maxRepliesPerComment, // Always 0
        ArrayList<String> postURLs,
        int resultsPerPage
) {
}