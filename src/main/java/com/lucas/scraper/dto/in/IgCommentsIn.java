package com.lucas.scraper.dto.in;

import java.util.List;

public record IgCommentsIn(

        List<String> directUrls,
        boolean includeNestedComments,
        boolean includeReplies,
        int resultsLimit
) {
    public IgCommentsIn(List<String> directUrls, boolean includeNestedComments, boolean includeReplies, int resultsLimit) {
        this.directUrls = directUrls;
        this.includeNestedComments = includeNestedComments;
        this.includeReplies = includeReplies;
        this.resultsLimit = resultsLimit;
    }
}
