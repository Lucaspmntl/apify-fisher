package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.List;

public record IgFisherDeltaCommentsIn(

        @URL @NotBlank String postUrl,
        @NotNull Integer resultsLimit,
        @NotNull List<String> lastCommentsIds
) {
}
