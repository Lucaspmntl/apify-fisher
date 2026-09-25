package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.apify.FbPostIn;
import com.lucas.scraper.dto.out.FbPostOut;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FbPostGatewayTest {

    @Test
    void getFacebookPost_doesNotThrow_whenItemHasMissingFields() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);

        RunOut run = new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
        when(feign.startRun(anyString(), org.mockito.ArgumentMatchers.any(), anyString())).thenReturn(run);

        // Item de dataset incompleto: sem publish_time e sem facebookUrl — regressão do que antes
        // derrubava a requisição inteira com NPE (ver FbPostGateway.java linha 54/58 antes do fix)
        Map<String, Object> incompleteItem = new HashMap<>();
        incompleteItem.put("id", "post-1");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(incompleteItem));

        FbPostGateway gateway = new FbPostGateway(feign, polling);
        gateway.actorId = "test-actor";
        gateway.token = "test-token";
        FbPostIn input = new FbPostIn(false, 5, List.of(new FbPostIn.StartUrls("https://facebook.com/x")));

        List<FbPostOut> result = assertDoesNotThrow(() -> gateway.getFacebookPost(input));

        assertEquals(1, result.size());
        assertEquals("post-1", result.getFirst().id());
        assertNull(result.getFirst().date());
        assertNull(result.getFirst().ownerUsername());
    }
}
