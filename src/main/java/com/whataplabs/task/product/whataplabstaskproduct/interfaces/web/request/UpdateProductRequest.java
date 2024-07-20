package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdateProductRequest(
        @Schema(description = "상품명", example = "item111")
        @NotBlank
        String name,
        @Schema(description = "가격", example = "1500")
        @NotNull
        @PositiveOrZero
        BigDecimal price,
        @Schema(description = "수량", example = "7")
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
