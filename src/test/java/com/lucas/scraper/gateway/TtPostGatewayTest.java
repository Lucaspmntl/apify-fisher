package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtPostIn;
import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TtPostGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static TtPostGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        TtPostGateway gateway = new TtPostGateway(feign, polling);
        gateway.actorId = "test-actor";
        gateway.token = "test-token";
        return gateway;
    }

    private static TtPostIn dummyInput() {
        return new TtPostIn(List.of("https://tiktok.com/@x/video/1"), 100, false, false, false, false, "NEVER_DOWNLOAD_SUBTITLES");
    }

    @Test
    void mapsOwnerFromNestedAuthorMeta() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> authorMeta = new HashMap<>();
        authorMeta.put("id", "u1");
        authorMeta.put("nickName", "fulano");
        Map<String, Object> raw = new HashMap<>();
        raw.put("id", "p1");
        raw.put("createTimeISO", "2026-01-01T10:00:00Z");
        raw.put("authorMeta", authorMeta);
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<TtPostOut> result = gatewayWith(feign, polling).getPost(dummyInput());

        assertEquals(1, result.size());
        assertEquals("fulano", result.getFirst().ownerUsername());
    }

    @Test
    void doesNotThrow_whenAuthorMetaAndVideoMetaAreMissing() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> raw = new HashMap<>();
        raw.put("id", "p1"); // sem authorMeta/videoMeta/createTimeISO
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        TtPostGateway gateway = gatewayWith(feign, polling);
        TtPostIn input = dummyInput();

        List<TtPostOut> result = assertDoesNotThrow(() -> gateway.getPost(input));
        assertNull(result.getFirst().ownerUsername());
        assertNull(result.getFirst().imageUrl());
    }
}
