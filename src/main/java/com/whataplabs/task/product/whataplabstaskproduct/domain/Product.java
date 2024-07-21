package com.whataplabs.task.product.whataplabstaskproduct.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    @Builder
    public Product(Long id, String name, BigDecimal price, int amount, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;

        changeName(name);
        changePrice(price);

        this.amount = amount;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    private void changeName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("상품명은 빈값일 수 없습니다.");
        }

        this.name = name;
    }

    private void changePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("유효하지 않은 가격입니다. price=" + price);
        }

        this.price = price;
    }
}
