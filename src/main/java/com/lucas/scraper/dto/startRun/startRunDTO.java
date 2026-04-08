package com.lucas.scraper.dto.startRun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record startRunDTO(
        Data data
) {
}
