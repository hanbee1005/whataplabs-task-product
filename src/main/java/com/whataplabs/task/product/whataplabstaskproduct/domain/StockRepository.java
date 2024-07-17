package com.whataplabs.task.product.whataplabstaskproduct.domain;

public interface StockRepository {
    int deduct(OrderedProduct product);
}
