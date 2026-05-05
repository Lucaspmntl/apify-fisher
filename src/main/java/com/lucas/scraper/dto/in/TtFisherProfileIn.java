package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;

public record TtFisherProfileIn(
        @NotBlank
        String profileId
) {
}
