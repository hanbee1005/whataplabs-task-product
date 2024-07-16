package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;

import java.math.BigDecimal;

public record AddProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer amount
) {
    public static AddProductResponse from(Product product) {
        return new AddProductResponse(product.getId(), product.getName(), product.getPrice(), product.getAmount());
    }
}
