package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record IgCommentsInput(

        @NotBlank
        List<String> directUrls,
        @NotBlank
        boolean includeNestedComments,
        @NotBlank
        boolean includeReplies,
        @NotNull
        int resultsLimit
) {
}
