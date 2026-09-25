package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtCommentsIn;
import com.lucas.scraper.dto.out.TtCommentsOut;
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

class TtCommentsGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static TtCommentsGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        TtCommentsGateway gateway = new TtCommentsGateway(feign, polling);
        gateway.actorId = "test-actor";
        gateway.token = "test-token";
        return gateway;
    }

    @Test
    void mapsKnownFields() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> raw = new HashMap<>();
        raw.put("cid", "c1");
        raw.put("text", "legal");
        raw.put("uniqueId", "fulano");
        raw.put("createTimeISO", "2026-01-01T10:00:00Z");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<TtCommentsOut> result = gatewayWith(feign, polling)
                .getComments(new TtCommentsIn(1000, false, 0, List.of("https://tiktok.com/@x/video/1"), 1000));

        assertEquals(1, result.size());
        assertEquals("c1", result.getFirst().id());
        assertEquals("fulano", result.getFirst().ownerUsername());
    }

    @Test
    void doesNotThrow_whenDateIsMissing() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> raw = new HashMap<>();
        raw.put("cid", "c1"); // sem "createTimeISO"
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        TtCommentsGateway gateway = gatewayWith(feign, polling);
        TtCommentsIn input = new TtCommentsIn(1000, false, 0, List.of("https://tiktok.com/@x/video/1"), 1000);

        List<TtCommentsOut> result = assertDoesNotThrow(() -> gateway.getComments(input));
        assertNull(result.getFirst().date());
    }
}
