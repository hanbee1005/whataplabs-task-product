package com.whataplabs.task.product.whataplabstaskproduct.domain.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends ProductBusinessException {
    private final Long productId;

    public InsufficientStockException(Long productId) {
        super("상품의 재고가 충분하지 않습니다. id=" + productId);
        this.productId = productId;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.INSUFFICIENT_STOCK;
    }

    @Override
    public <T> T getData() {
        return (T) productId;
    }
}
