package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbProfileIn;
import com.lucas.scraper.dto.out.FbProfileOut;
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

class FbProfileGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static FbProfileGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        FbProfileGateway gateway = new FbProfileGateway(feign, polling);
        gateway.actorId = "test-actor";
        gateway.token = "test-token";
        return gateway;
    }

    @Test
    void mapsKnownFields_fromNestedProfileObject() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> profile = new HashMap<>();
        profile.put("profile_id", "p1");
        profile.put("name", "fulano");
        profile.put("intro", "bio");
        profile.put("image", "http://pic");
        profile.put("url", "http://facebook.com/fulano");
        Map<String, Object> raw = new HashMap<>();
        raw.put("profile", profile);
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<FbProfileOut> result = gatewayWith(feign, polling)
                .getFacebookProfile(new FbProfileIn("details_by_url", 0, "http://facebook.com/fulano"));

        assertEquals(1, result.size());
        assertEquals("p1", result.getFirst().id());
    }

    @Test
    void doesNotThrow_andDropsItem_whenProfileObjectIsMissing() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        // Item sem o campo "profile" — o actor às vezes retorna isso em caso de falha externa
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(new HashMap<>()));

        FbProfileGateway gateway = gatewayWith(feign, polling);
        FbProfileIn input = new FbProfileIn("details_by_url", 0, "http://facebook.com/fulano");

        List<FbProfileOut> result = assertDoesNotThrow(() -> gateway.getFacebookProfile(input));
        assertTrue(result.isEmpty());
    }
}
