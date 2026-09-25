package com.lucas.scraper.utils;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RawItemMapperTest {

    @Test
    void getString_returnsNull_whenKeyIsMissing() {
        assertNull(RawItemMapper.getString(Map.of(), "text"));
    }

    @Test
    void getInteger_returnsNull_insteadOfThrowing_whenFieldIsMissing() {
        // Regressão: antes era (Integer) raw.get("count"), que lançava NPE ao desempacotar null
        assertNull(RawItemMapper.getInteger(Map.of(), "count"));
    }

    @Test
    void getInteger_acceptsLong_insteadOfThrowingClassCastException() {
        // Regressão: Jackson pode desserializar um número grande como Long, não Integer
        Map<String, Object> raw = new HashMap<>();
        raw.put("count", 10_000_000_000L);
        assertEquals(1410065408, RawItemMapper.getInteger(raw, "count")); // overflow esperado do intValue(), mas sem exception
    }

    @Test
    void getMap_returnsNull_whenKeyIsMissingOrWrongType() {
        assertNull(RawItemMapper.getMap(Map.of(), "owner"));
        assertNull(RawItemMapper.getMap(Map.of("owner", "not-a-map"), "owner"));
    }

    @Test
    void getIsoDate_returnsNull_insteadOfThrowing_whenFieldIsMissing() {
        // Regressão: antes era OffsetDateTime.parse((String) raw.get("date")), NPE se ausente
        assertNull(RawItemMapper.getIsoDate(Map.of(), "date"));
    }

    @Test
    void getIsoDate_returnsNull_insteadOfThrowing_whenValueIsMalformed() {
        assertNull(RawItemMapper.getIsoDate(Map.of("date", "not-a-date"), "date"));
    }

    @Test
    void getIsoDate_parsesValidIsoString() {
        OffsetDateTime date = RawItemMapper.getIsoDate(Map.of("date", "2026-01-01T10:00:00Z"), "date");
        assertEquals(2026, date.getYear());
    }

    @Test
    void getEpochSecondsDate_returnsNull_insteadOfThrowing_whenFieldIsMissing() {
        // Regressão: antes era Instant.ofEpochSecond((Integer) raw.get("publish_time")), NPE se ausente
        assertDoesNotThrow(() -> assertNull(RawItemMapper.getEpochSecondsDate(Map.of(), "publish_time")));
    }

    @Test
    void getFirstPathSegment_returnsNull_insteadOfThrowing_whenUrlIsNull() {
        // Regressão: antes era URI.create(postUrl).getPath().split("/")[1], NPE se postUrl nulo
        assertNull(RawItemMapper.getFirstPathSegment(null));
    }

    @Test
    void getFirstPathSegment_returnsNull_insteadOfThrowing_whenUrlHasNoSegments() {
        // Regressão: antes era split("/")[1], ArrayIndexOutOfBoundsException se não houver 2º segmento
        assertNull(RawItemMapper.getFirstPathSegment("https://facebook.com"));
    }

    @Test
    void getFirstPathSegment_extractsUsername() {
        assertEquals("someuser", RawItemMapper.getFirstPathSegment("https://facebook.com/someuser/posts/123"));
    }
}
