package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProductRequest(
        @NotBlank
        String name,
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @PositiveOrZero
        Integer amount
) {
    public UpdateProductRequest {
        if (ObjectUtils.isEmpty(amount)) {
            amount = 0;
        }
    }

    public Product to(Long id) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .amount(amount)
                .lastModifiedAt(LocalDateTime.now())
                .build();
    }
}
