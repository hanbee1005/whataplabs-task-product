package com.whataplabs.task.product.whataplabstaskproduct.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int amount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
