package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record IgFisherPostIn(
        @URL
        @NotBlank
        String postUrl
) {
}
