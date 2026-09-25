package com.lucas.scraper.service;

import com.lucas.scraper.dto.out.TtPostOut;
import com.lucas.scraper.exception.InvalidURLException;
import com.lucas.scraper.gateway.TtCommentsGateway;
import com.lucas.scraper.gateway.TtPostGateway;
import com.lucas.scraper.gateway.TtProfileGateway;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TiktokServiceTest {

    @Test
    void getTiktokComments_throwsInvalidURLException_whenUrlIsNotFromTikTok() {
        TiktokService service = new TiktokService(mock(TtCommentsGateway.class), mock(TtPostGateway.class), mock(TtProfileGateway.class));

        assertThrows(InvalidURLException.class, () -> service.getTiktokComments("https://instagram.com/x"));
    }

    @Test
    void getTikTokProfile_throwsInvalidURLException_whenProfileIdIsBlank() {
        TiktokService service = new TiktokService(mock(TtCommentsGateway.class), mock(TtPostGateway.class), mock(TtProfileGateway.class));

        assertThrows(InvalidURLException.class, () -> service.getTikTokProfile(""));
    }

    @Test
    void getTiktokPost_delegatesToGateway_whenUrlIsValid() {
        TtPostGateway postGateway = mock(TtPostGateway.class);
        TtPostOut post = new TtPostOut("p1", "texto", null, 1, 2, "https://tiktok.com/@x/video/1", null, "u1", "fulano", "Fulano");
        when(postGateway.getPost(any())).thenReturn(List.of(post));

        TiktokService service = new TiktokService(mock(TtCommentsGateway.class), postGateway, mock(TtProfileGateway.class));

        TtPostOut result = service.getTiktokPost("https://tiktok.com/@x/video/1");

        assertEquals("p1", result.id());
    }
}
