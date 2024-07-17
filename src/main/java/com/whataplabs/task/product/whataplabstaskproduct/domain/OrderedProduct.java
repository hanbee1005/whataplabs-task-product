package com.whataplabs.task.product.whataplabstaskproduct.domain;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderedProduct {
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

    public BigDecimal getOrderPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
