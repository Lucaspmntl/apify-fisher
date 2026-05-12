package com.lucas.scraper.dto.out;

import java.util.List;

public record IgCommentsDeltaOut(

    boolean deltaFound,
    List<IgCommentsOut> comments

) {
}
