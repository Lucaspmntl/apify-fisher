package com.lucas.scraper.service;

import com.lucas.scraper.dto.out.FbCommentsOut;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.FbCommentsGateway;
import com.lucas.scraper.gateway.FbPostGateway;
import com.lucas.scraper.gateway.FbProfileGateway;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FacebookServiceTest {

    @Test
    void getFacebookComments_throwsInvalidURLException_whenUrlIsNotFromFacebook() {
        FacebookService service = new FacebookService(mock(FbCommentsGateway.class), mock(FbPostGateway.class), mock(FbProfileGateway.class));

        assertThrows(InvalidURLException.class, () -> service.getFacebookComments("https://instagram.com/x"));
    }

    @Test
    void getFacebookComments_delegatesToGateway_whenUrlIsValid() {
        FbCommentsGateway commentsGateway = mock(FbCommentsGateway.class);
        FbCommentsOut comment = new FbCommentsOut("c1", "texto", null, "fulano", "u1", null);
        when(commentsGateway.getFacebookComments(any())).thenReturn(List.of(comment));

        FacebookService service = new FacebookService(commentsGateway, mock(FbPostGateway.class), mock(FbProfileGateway.class));

        List<FbCommentsOut> result = service.getFacebookComments("https://facebook.com/post/1");

        assertEquals(1, result.size());
        assertEquals("c1", result.getFirst().id());
    }

    @Test
    void getFacebookProfile_throwsInvalidURLException_whenProfileIdIsBlank() {
        FacebookService service = new FacebookService(mock(FbCommentsGateway.class), mock(FbPostGateway.class), mock(FbProfileGateway.class));

        assertThrows(InvalidURLException.class, () -> service.getFacebookProfile("  "));
    }
}
