package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AddProductRequest(
        @Schema(description = "상품명", example = "item1")
        @NotBlank
        String name,
        @Schema(description = "가격", example = "1000")
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @Schema(description = "수량", example = "5")
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
