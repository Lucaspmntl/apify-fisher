package com.lucas.scraper.dto.response.startRun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApifyRunResponse(
        Data data
) {
}
