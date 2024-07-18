package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response;

import java.util.List;

public record ProductOrderResponse(
        List<Long> products
) {
}
