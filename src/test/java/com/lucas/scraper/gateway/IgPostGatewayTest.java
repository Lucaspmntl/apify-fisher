package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgPostIn;
import com.lucas.scraper.dto.out.IgPostOut;
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

class IgPostGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static IgPostGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        IgPostGateway gateway = new IgPostGateway(feign, polling);
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
        raw.put("id", "p1");
        raw.put("caption", "legenda");
        raw.put("timestamp", "2026-01-01T10:00:00Z");
        raw.put("commentsCount", 10);
        raw.put("likesCount", 20);
        raw.put("url", "https://instagram.com/p/p1");
        raw.put("ownerUsername", "fulano");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<IgPostOut> result = gatewayWith(feign, polling)
                .getInstagramPost(new IgPostIn("basicData", 24, false, List.of("https://instagram.com/p/p1")));

        assertEquals(1, result.size());
        assertEquals("p1", result.getFirst().id());
        assertEquals(10, result.getFirst().commentsCount());
    }

    @Test
    void doesNotThrow_whenLikesAndCommentsCountAreMissing() {
        // Regressão: o compact constructor de IgPostOut comparava likesCount/commentsCount com 0
        // sem checar null antes, o que quebrava com NPE quando o campo vinha ausente do Apify
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> raw = new HashMap<>();
        raw.put("id", "p1");
        raw.put("url", "https://instagram.com/p/p1");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        IgPostGateway gateway = gatewayWith(feign, polling);
        IgPostIn input = new IgPostIn("basicData", 24, false, List.of("https://instagram.com/p/p1"));

        List<IgPostOut> result = assertDoesNotThrow(() -> gateway.getInstagramPost(input));
        assertNull(result.getFirst().commentsCount());
        assertNull(result.getFirst().likesCount());
    }
}
