package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;

import java.util.List;

public record ProductOrderRequest(
        List<OrderedProductRequest> orderProducts
) {
    public List<OrderedProduct> orderedProducts() {
        return orderProducts.stream().map(OrderedProductRequest::to).toList();
    }
}
