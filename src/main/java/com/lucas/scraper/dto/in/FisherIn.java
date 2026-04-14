package com.lucas.scraper.dto.in;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record FisherIn(

        @NotBlank
        @URL String instagramUrl,

        @URL String tikTokUrl,
        @URL String facebookUrl,

        String keyword
) {
}
