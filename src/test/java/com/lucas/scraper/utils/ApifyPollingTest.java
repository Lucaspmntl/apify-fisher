package com.lucas.scraper.utils;

import com.lucas.scraper.dto.out.RunOut;
import com.lucas.scraper.exception.PollingFailedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApifyPollingTest {

    private static RunOut runWithStatus(String status) {
        return new RunOut(new RunOut.Data("run1", "act", "user", "task", "start", "end", status, "msg"));
    }

    private static ApifyPolling pollingWithMockedFeign(ApifyGenericFeign feign) {
        ApifyPolling polling = new ApifyPolling(feign);
        polling.token = "test-token";
        return polling;
    }

    @Test
    void waitForSucceeded_throwsPollingFailedException_whenRunFails() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        when(feign.getRunDetails(eq("run1"), anyString())).thenReturn(runWithStatus("FAILED"));

        ApifyPolling polling = pollingWithMockedFeign(feign);

        PollingFailedException exception = assertThrows(PollingFailedException.class,
                () -> polling.waitForSucceeded("run1"));
        assertEquals(502, exception.getDetails().statusCode());
    }

    @Test
    void waitForSucceeded_throwsPollingFailedException_whenRunIsAborted() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        when(feign.getRunDetails(eq("run1"), anyString())).thenReturn(runWithStatus("ABORTED"));

        ApifyPolling polling = pollingWithMockedFeign(feign);

        assertThrows(PollingFailedException.class, () -> polling.waitForSucceeded("run1"));
    }

    @Test
    void waitForSucceeded_returnsNormally_whenRunSucceeds() {
        ApifyGenericFeign feign = mock(ApifyGenericFeign.class);
        when(feign.getRunDetails(eq("run1"), anyString())).thenReturn(runWithStatus("SUCCEEDED"));

        ApifyPolling polling = pollingWithMockedFeign(feign);

        assertDoesNotThrow(() -> polling.waitForSucceeded("run1"));
    }
}
