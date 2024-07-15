package com.whataplabs.task.product.whataplabstaskproduct.domain;

public class ProductNotFoundException extends RuntimeException {
    private final Long id;

    public ProductNotFoundException(Long id) {
        super("존재하지 않는 상품입니다. id=" + id);
        this.id = id;
    }
}
