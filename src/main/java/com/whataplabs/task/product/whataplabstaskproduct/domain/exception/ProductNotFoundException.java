package com.whataplabs.task.product.whataplabstaskproduct.domain.exception;

public class ProductNotFoundException extends ProductBusinessException {
    private final Long id;

    public ProductNotFoundException(Long id) {
        super("존재하지 않는 상품입니다. id=" + id);
        this.id = id;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.NOT_FOUND_PRODUCT;
    }

    @Override
    public <T> T getData() {
        return (T) id;
    }
}
