package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.IgProfileIn;
import com.lucas.scraper.dto.out.IgProfileOut;
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

class IgProfileGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static IgProfileGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        IgProfileGateway gateway = new IgProfileGateway(feign, polling);
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
        raw.put("id", "u1");
        raw.put("username", "fulano");
        raw.put("private", true);
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<IgProfileOut> result = gatewayWith(feign, polling)
                .getInstagramProfile(new IgProfileIn(false, List.of("fulano")));

        assertEquals(1, result.size());
        assertEquals("fulano", result.getFirst().username());
        assertEquals(true, result.getFirst().isPrivate());
    }

    @Test
    void doesNotThrow_whenPrivateFlagIsMissing() {
        // Regressão: IgProfileOut.isPrivate era boolean primitivo, um Boolean nulo vindo do
        // RawItemMapper quebrava com NPE ao desempacotar no construtor do record
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> raw = new HashMap<>();
        raw.put("id", "u1"); // sem "private"
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        IgProfileGateway gateway = gatewayWith(feign, polling);
        IgProfileIn input = new IgProfileIn(false, List.of("fulano"));

        List<IgProfileOut> result = assertDoesNotThrow(() -> gateway.getInstagramProfile(input));
        assertNull(result.getFirst().isPrivate());
    }
}
