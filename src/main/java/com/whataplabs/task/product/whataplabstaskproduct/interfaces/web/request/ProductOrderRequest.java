package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;

import javax.validation.Valid;
import java.util.List;

public record ProductOrderRequest(
        List<@Valid OrderedProductRequest> orderProducts
) {
    public List<OrderedProduct> orderedProducts() {
        return orderProducts.stream().map(OrderedProductRequest::to).toList();
    }
}
