package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;

public record FbFisherProfileIn(
        @NotBlank
        String profileId
) {
}
