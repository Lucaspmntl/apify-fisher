package com.lucas.scraper.dto.out.startRun;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RunResponseOut(
        Data data
) {
}
