package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record IgProfileIn(
        boolean includeAboutSection,
        List<String> usernames
) {
}
