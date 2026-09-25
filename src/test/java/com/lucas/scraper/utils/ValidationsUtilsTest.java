package com.lucas.scraper.utils;

import com.lucas.scraper.exception.InvalidResponseDataException;
import com.lucas.scraper.exception.InvalidURLException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationsUtilsTest {

    @Test
    void validatePlatformUrl_throws_whenUrlIsNull() {
        assertThrows(InvalidURLException.class, () -> ValidationsUtils.validatePlatformUrl(null, "Facebook"));
    }

    @Test
    void validatePlatformUrl_throws_whenUrlIsBlank() {
        assertThrows(InvalidURLException.class, () -> ValidationsUtils.validatePlatformUrl("   ", "Facebook"));
    }

    @Test
    void validatePlatformUrl_throws_whenUrlDoesNotContainPlatform() {
        assertThrows(InvalidURLException.class,
                () -> ValidationsUtils.validatePlatformUrl("https://instagram.com/foo", "Facebook"));
    }

    @Test
    void validatePlatformUrl_passes_whenUrlContainsPlatform() {
        assertDoesNotThrow(() -> ValidationsUtils.validatePlatformUrl("https://facebook.com/foo", "Facebook"));
    }

    @Test
    void requireNonBlank_throws_whenNullOrBlank() {
        assertThrows(InvalidURLException.class, () -> ValidationsUtils.requireNonBlank(null, "username"));
        assertThrows(InvalidURLException.class, () -> ValidationsUtils.requireNonBlank("  ", "username"));
    }

    @Test
    void parseDataVality_throws_whenListIsEmpty() {
        assertThrows(InvalidResponseDataException.class, () -> ValidationsUtils.parseDataVality(List.of()));
    }

    @Test
    void parseDataVality_throws_whenListIsNull() {
        assertThrows(InvalidResponseDataException.class, () -> ValidationsUtils.parseDataVality(null));
    }
}
