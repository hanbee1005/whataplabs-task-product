package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.exception;

import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ErrorType;

public record ErrorResponse(
        String code,
        String message
) {
    public static ErrorResponse createInternalServerError() {
        return new ErrorResponse(ErrorType.INTERNAL_SERVER_ERROR.getCode(), ErrorType.INTERNAL_SERVER_ERROR.getDefaultMessage());
    }
}
