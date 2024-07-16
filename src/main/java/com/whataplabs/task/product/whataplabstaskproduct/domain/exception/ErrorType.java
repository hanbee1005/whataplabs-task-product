package com.whataplabs.task.product.whataplabstaskproduct.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    INTERNAL_SERVER_ERROR(500001, "INTERNAL_SERVER_ERROR", "알 수 없는 서버 에러가 발생하였습니다."),

    BAD_REQUEST(400001, "BAD_REQUEST", "잘못된 요청 파라미터입니다."),
    INVALID_PRODUCT_NAME(400101, "INVALID_PRODUCT_NAME", "유효하지 않은 상품명입니다."),
    INVALID_PRODUCT_PRICE(400102, "INVALID_PRODUCT_PRICE", "유효하지 않은 상품 가격입니다."),
    INVALID_PRODUCT_AMOUNT(400103, "INVALID_PRODUCT_AMOUNT", "유효하지 않은 상품 수량입니다."),

    NOT_FOUND_PRODUCT(400201, "NOT_FOUND_PRODUCT", "존재하지 않는 상품입니다."),
    ;

    private final int status;
    private final String code;
    private final String defaultMessage;
}
