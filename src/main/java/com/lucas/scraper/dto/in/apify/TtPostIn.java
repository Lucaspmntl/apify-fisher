package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record TtPostIn(
        List<String> postURLs,
        int resultsPerPage,
        boolean scrapeRelatedVideos,
        boolean shouldDownloadCovers,
        boolean shouldDownloadSlideshowImages,
        boolean shouldDownloadVideos,
        String downloadSubtitlesOptions
) {
}
