package com.lucas.scraper.service;

import com.lucas.scraper.dto.in.IgFisherDeltaCommentsIn;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.IgCommentsGateway;
import com.lucas.scraper.gateway.IgPostGateway;
import com.lucas.scraper.gateway.IgProfileGateway;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class InstagramServiceTest {

    private InstagramService service() {
        return new InstagramService(mock(IgCommentsGateway.class), mock(IgProfileGateway.class), mock(IgPostGateway.class));
    }

    @Test
    void getInstagramComments_throwsInvalidURLException_whenUrlIsNotFromInstagram() {
        assertThrows(InvalidURLException.class, () -> service().getInstagramComments("https://facebook.com/x"));
    }

    @Test
    void getInstagramProfile_throwsInvalidURLException_whenUsernameIsBlank() {
        assertThrows(InvalidURLException.class, () -> service().getInstagramProfile("  "));
    }

    @Test
    void getDeltaInstagramComments_throwsInvalidURLException_whenUrlIsNotFromInstagram() {
        IgFisherDeltaCommentsIn input = new IgFisherDeltaCommentsIn("https://tiktok.com/x", 100, List.of("id1"));

        assertThrows(InvalidURLException.class, () -> service().getDeltaInstagramComments(input));
    }
}
