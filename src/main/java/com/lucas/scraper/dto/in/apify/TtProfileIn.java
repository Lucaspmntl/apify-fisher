package com.lucas.scraper.dto.in.apify;

import java.util.List;

public record TtProfileIn(
        boolean excludePinnedPosts,
        List<String> profiles,
        boolean shouldDownloadAvatars,
        boolean shouldDownloadCovers,
        boolean shouldDownloadSlideshowImages,
        boolean shouldDownloadVideos,
        List<String> profileScrapeSections,
        String profileSorting,
        int resultsPerPage,
        String downloadSubtitlesOptions
) {
}
