package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record OrderedProductRequest(
        @Schema(description = "상품 아이디", example = "101")
        @NotNull
        Long productId,
        @Schema(description = "수량", example = "3")
        int quantity,
        @Schema(description = "가격", example = "1000")
        @PositiveOrZero
        BigDecimal unitPrice
) {
    public OrderedProduct to() {
        return OrderedProduct.builder().productId(productId).quantity(quantity).unitPrice(unitPrice).build();
    }
}
