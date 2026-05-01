package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;

public record IgFisherProfileIn(
        @NotBlank
        String username
) {
}
