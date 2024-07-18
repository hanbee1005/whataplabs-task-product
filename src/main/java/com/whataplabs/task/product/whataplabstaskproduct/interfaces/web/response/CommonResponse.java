package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response;

import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ProductBusinessException;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.exception.ErrorResponse;

public record CommonResponse<T>(
        String code,
        String message,
        T data
) {
    private static final  String SUCCESS_CODE = "OK";
    private static final  String SUCCESS_MESSAGE = "OK";

    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static <T> CommonResponse<T> of(ProductBusinessException exception) {
        return new CommonResponse<>(exception.getErrorCode(), exception.getErrorMessage(), exception.getData());
    }

    public static <T> CommonResponse<T> of(ErrorResponse response) {
        return new CommonResponse<>(response.code(), response.message(), null);
    }
}
