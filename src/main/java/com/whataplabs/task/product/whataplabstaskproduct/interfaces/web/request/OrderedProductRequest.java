package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;

import java.math.BigDecimal;

public record OrderedProductRequest(
        Long productId,
        int quantity,
        BigDecimal unitPrice
) {
    public OrderedProduct to() {
        return OrderedProduct.builder().productId(productId).quantity(quantity).unitPrice(unitPrice).build();
    }
}
