package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductEntityTest {

    @Test
    @DisplayName("상품 등록 시 이름은 빈값일 수 없습니다.")
    public void createFailByEmptyName() {
        // given
        Product product = Product.builder().price(BigDecimal.valueOf(1234)).amount(12).build();

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> ProductEntity.create(product), "상품명은 빈값일 수 없습니다.");
    }

    @Test
    @DisplayName("상품 수정 시 이름은 빈값일 수 없습니다.")
    public void updateFailByEmptyName() {
        // given
        Product product = Product.builder().name("test item 1").price(BigDecimal.valueOf(1234)).amount(12).build();
        ProductEntity entity = ProductEntity.create(product);

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> entity.update(Product.builder().price(BigDecimal.valueOf(567)).amount(34).build()),
                "상품명은 빈값일 수 없습니다.");
    }

    @Test
    @DisplayName("상품 등록 시 가격은 빈값 또는 음수일 수 없습니다.")
    public void createFailByInvalidPrice() {
        // given
        Product product = Product.builder().name("test item 1").amount(12).build();

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> ProductEntity.create(product), "유효하지 않은 가격입니다. price=" + null);
    }

    @Test
    @DisplayName("상품 수정 시 가격은 빈값 또는 음수일 수 없습니다.")
    public void updateFailByInvalidPrice() {
        // given
        Product product = Product.builder().name("test item 1").price(BigDecimal.valueOf(1234)).amount(12).build();
        ProductEntity entity = ProductEntity.create(product);

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> entity.update(Product.builder().name("test item 1").price(BigDecimal.valueOf(-123)).amount(34).build()),
                "유효하지 않은 가격입니다. price=" + -123);
    }

    @Test
    @DisplayName("상품 등록 시 수량은 음수일 수 없습니다.")
    public void createFailByNegativeAmount() {
        // given
        Product product = Product.builder().name("test item 1").price(BigDecimal.valueOf(1234)).amount(-12).build();

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> ProductEntity.create(product), "수량은 0 또는 양수여야 합니다. amount=" + -12);
    }

    @Test
    @DisplayName("상품 수정 시 수량은 음수일 수 없습니다.")
    public void updateFailByNegativeAmount() {
        // given
        Product product = Product.builder().name("test item 1").price(BigDecimal.valueOf(1234)).amount(12).build();
        ProductEntity entity = ProductEntity.create(product);

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> entity.update(Product.builder().name("test item 1").price(BigDecimal.valueOf(567)).amount(-34).build()),
                "수량은 0 또는 양수여야 합니다. amount=" + -34);
    }

}