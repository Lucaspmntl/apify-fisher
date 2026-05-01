package com.lucas.scraper.dto.out.exception;

import java.util.List;

public record BeansValidationOut(
        String message,
        int status,
        List<FieldExceptionOut>errors
) {
}
