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

class TikTokServiceTest {

    @Test
    void getTikTokComments_throwsInvalidURLException_whenUrlIsNotFromTikTok() {
        TikTokService service = new TikTokService(mock(TtCommentsGateway.class), mock(TtPostGateway.class), mock(TtProfileGateway.class));

        assertThrows(InvalidURLException.class, () -> service.getTikTokComments("https://instagram.com/x"));
    }

    @Test
    void getTikTokProfile_throwsInvalidURLException_whenProfileIdIsBlank() {
        TikTokService service = new TikTokService(mock(TtCommentsGateway.class), mock(TtPostGateway.class), mock(TtProfileGateway.class));

        assertThrows(InvalidURLException.class, () -> service.getTikTokProfile(""));
    }

    @Test
    void getTikTokPost_delegatesToGateway_whenUrlIsValid() {
        TtPostGateway postGateway = mock(TtPostGateway.class);
        TtPostOut post = new TtPostOut("p1", "texto", null, 1, 2, "https://tiktok.com/@x/video/1", null, "u1", "fulano", "Fulano");
        when(postGateway.getPost(any())).thenReturn(List.of(post));

        TikTokService service = new TikTokService(mock(TtCommentsGateway.class), postGateway, mock(TtProfileGateway.class));

        TtPostOut result = service.getTikTokPost("https://tiktok.com/@x/video/1");

        assertEquals("p1", result.id());
    }
}
