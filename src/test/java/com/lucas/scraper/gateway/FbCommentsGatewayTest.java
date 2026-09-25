package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbCommentsIn;
import com.lucas.scraper.dto.out.FbCommentsOut;
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

class FbCommentsGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static FbCommentsGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        FbCommentsGateway gateway = new FbCommentsGateway(feign, polling);
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
        raw.put("commentId", "c1");
        raw.put("text", "ótimo post");
        raw.put("date", "2026-01-01T10:00:00Z");
        raw.put("profileName", "fulano");
        raw.put("profileId", "u1");
        raw.put("profilePicture", "http://pic");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<FbCommentsOut> result = gatewayWith(feign, polling)
                .getFacebookComments(new FbCommentsIn(false, 5, List.of(new FbCommentsIn.StartUrls("https://facebook.com/x"))));

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
        raw.put("commentId", "c1"); // sem "date"
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        FbCommentsGateway gateway = gatewayWith(feign, polling);
        FbCommentsIn input = new FbCommentsIn(false, 5, List.of(new FbCommentsIn.StartUrls("https://facebook.com/x")));

        List<FbCommentsOut> result = assertDoesNotThrow(() -> gateway.getFacebookComments(input));
        assertNull(result.getFirst().date());
    }
}
