package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.TtProfileIn;
import com.lucas.scraper.dto.out.TtProfileOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TtProfileGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static TtProfileGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        TtProfileGateway gateway = new TtProfileGateway(feign, polling);
        gateway.actorId = "test-actor";
        gateway.token = "test-token";
        return gateway;
    }

    private static TtProfileIn dummyInput() {
        return new TtProfileIn(false, List.of("fulano"), false, false, false, false, List.of("videos"), "latest", 5, "NEVER_DOWNLOAD_SUBTITLES");
    }

    @Test
    void mapsKnownFields_fromNestedAuthorMeta() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> authorMeta = new HashMap<>();
        authorMeta.put("id", "u1");
        authorMeta.put("name", "fulano");
        authorMeta.put("privateAccount", true);
        Map<String, Object> raw = new HashMap<>();
        raw.put("authorMeta", authorMeta);
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<TtProfileOut> result = gatewayWith(feign, polling).getProfile(dummyInput());

        assertEquals(1, result.size());
        assertEquals("fulano", result.getFirst().username());
        assertEquals(true, result.getFirst().isPrivate());
    }

    @Test
    void doesNotThrow_andDropsItem_whenAuthorMetaIsMissing() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(new HashMap<>()));

        TtProfileGateway gateway = gatewayWith(feign, polling);
        TtProfileIn input = dummyInput();

        List<TtProfileOut> result = assertDoesNotThrow(() -> gateway.getProfile(input));
        assertTrue(result.isEmpty());
    }
}
