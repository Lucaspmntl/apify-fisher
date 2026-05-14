package com.lucas.scraper.dto.out;

import com.lucas.scraper.utils.ValidatablePayload;

import java.util.List;

public record IgCommentsDeltaOut(

    boolean deltaFound,
    List<IgCommentsOut> comments

) implements ValidatablePayload {

    @Override
    public boolean isBlankPayload() {
        return deltaFound && comments.isEmpty();
    }
}
