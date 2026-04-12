package com.lucas.scraper.dto.request;

public record ApifyFilterCommentsRequestDTO(
        ApifyCommentsRequestDTO apifyInput,
        String stringFilter
) {
}
