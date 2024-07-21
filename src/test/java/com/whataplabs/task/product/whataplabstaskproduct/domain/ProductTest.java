package com.whataplabs.task.product.whataplabstaskproduct.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    @DisplayName("상품 등록 시 이름은 빈값일 수 없습니다.")
    public void createFailByEmptyName() {
        // given
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> Product.builder().price(BigDecimal.valueOf(1234)).amount(12).build(),
                "상품명은 빈값일 수 없습니다.");
    }

    @Test
    @DisplayName("상품 등록 시 가격은 빈값 또는 음수일 수 없습니다.")
    public void createFailByInvalidPrice() {
        // given
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> Product.builder().name("test item 1").amount(12).build(),
                "유효하지 않은 가격입니다. price=" + null);
    }

}