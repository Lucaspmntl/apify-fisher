package com.lucas.scraper.gateway;

import com.lucas.scraper.dto.in.IgFisherDeltaCommentsIn;
import com.lucas.scraper.dto.in.apify.IgCommentsIn;
import com.lucas.scraper.dto.out.IgCommentsDeltaOut;
import com.lucas.scraper.dto.out.IgCommentsOut;
import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.utils.ApifyGenericFeign;
import com.lucas.scraper.utils.ApifyPolling;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IgCommentsGatewayTest {

    private static RunOut succeededRun() {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", "SUCCEEDED", null));
    }

    private static IgCommentsGateway gatewayWith(ApifyGenericFeign feign, ApifyPolling polling) {
        IgCommentsGateway gateway = new IgCommentsGateway(feign, polling);
        gateway.actorId = "test-actor";
        gateway.token = "test-token";
        return gateway;
    }

    @Test
    void mapsOwnerFromNestedObject() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> owner = new HashMap<>();
        owner.put("id", "u1");
        owner.put("full_name", "fulano");
        Map<String, Object> raw = new HashMap<>();
        raw.put("id", "c1");
        raw.put("text", "legal");
        raw.put("timestamp", "2026-01-01T10:00:00Z");
        raw.put("owner", owner);
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        List<IgCommentsOut> result = gatewayWith(feign, polling)
                .getInstagramComments(new IgCommentsIn(List.of("https://instagram.com/x"), false, false, 1000));

        assertEquals(1, result.size());
        assertEquals("fulano", result.getFirst().ownerUsername());
    }

    @Test
    void doesNotThrow_whenOwnerIsMissing() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> raw = new HashMap<>();
        raw.put("id", "c1"); // sem "owner" nem "timestamp"
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(raw));

        IgCommentsGateway gateway = gatewayWith(feign, polling);
        IgCommentsIn input = new IgCommentsIn(List.of("https://instagram.com/x"), false, false, 1000);

        List<IgCommentsOut> result = assertDoesNotThrow(() -> gateway.getInstagramComments(input));
        assertNull(result.getFirst().ownerId());
        assertNull(result.getFirst().date());
    }

    @Test
    void delta_abortsRunAndStopsAtKnownCommentId() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());

        Map<String, Object> newComment = new HashMap<>();
        newComment.put("id", "new-1");
        Map<String, Object> knownComment = new HashMap<>();
        knownComment.put("id", "known-1");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(newComment, knownComment));

        IgCommentsGateway gateway = gatewayWith(feign, polling);
        IgFisherDeltaCommentsIn input = new IgFisherDeltaCommentsIn("https://instagram.com/x", 100, List.of("known-1"));

        IgCommentsDeltaOut result = gateway.getDeltaInstagramComments(input);

        assertTrue(result.deltaFound());
        assertEquals(1, result.comments().size());
        assertEquals("new-1", result.comments().getFirst().id());
        verify(feign, times(1)).abortRun(anyString(), anyString());
    }

    @Test
    void delta_returnsAllScraped_whenKnownIdNeverAppearsAndRunFinishes() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        ApifyPolling polling = mock(ApifyPolling.class);
        when(feign.startRun(anyString(), any(), anyString())).thenReturn(succeededRun());
        when(polling.isStatusRunning(anyString())).thenReturn(false);

        Map<String, Object> comment = new HashMap<>();
        comment.put("id", "unrelated-1");
        when(feign.getDatasetItems(anyString(), anyString())).thenReturn(List.of(comment));

        IgCommentsGateway gateway = gatewayWith(feign, polling);
        IgFisherDeltaCommentsIn input = new IgFisherDeltaCommentsIn("https://instagram.com/x", 100, List.of("never-seen"));

        IgCommentsDeltaOut result = gateway.getDeltaInstagramComments(input);

        assertFalse(result.deltaFound());
        assertEquals(1, result.comments().size());
    }
}
