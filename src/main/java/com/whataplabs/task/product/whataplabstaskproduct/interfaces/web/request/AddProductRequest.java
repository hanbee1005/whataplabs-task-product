package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddProductRequest(
        @NotBlank
        String name,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @PositiveOrZero
        Integer amount
) {
    public AddProductRequest {
        if (ObjectUtils.isEmpty(amount)) {
            amount = 0;
        }
    }

    public Product to() {
        return Product.builder()
                .name(name)
                .price(price)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
