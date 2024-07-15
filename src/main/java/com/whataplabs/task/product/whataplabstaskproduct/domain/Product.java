package com.whataplabs.task.product.whataplabstaskproduct.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
