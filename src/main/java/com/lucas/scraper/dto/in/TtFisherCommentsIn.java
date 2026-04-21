package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record TtFisherCommentsIn(
        @URL
        @NotBlank
        String postUrl
) {
}
