package com.lucas.scraper.dto.request;

import java.util.List;

public record CommentScraperRequestDTO(
        List<String> directUrls,
        boolean includeNestedComments,
        boolean includeReplies,
        int resultsLimit
) {
}
