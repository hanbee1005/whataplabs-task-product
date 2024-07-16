package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;

import java.math.BigDecimal;

public record GetProductResponse(
        Long id,
        String name,
        BigDecimal price,
        int amount
) {
    public static GetProductResponse from(Product product) {
        return new GetProductResponse(product.getId(), product.getName(), product.getPrice(), product.getAmount());
    }
}
