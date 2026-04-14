package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IgKeywordIn(

        @NotNull
        IgCommentsInput apifyInput,
        @NotBlank
        @Size(min = 1, max = 100, message = "")
        String keyword
) {
}
