package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;
    private BigDecimal price;
    private Integer amount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static ProductEntity create(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.name = product.getName();
        entity.price = product.getPrice();
        entity.amount = product.getAmount();
        entity.createdAt = product.getCreatedAt();
        return entity;
    }

    public void update(Product product) {
        if (!ObjectUtils.isEmpty(product.getName())) {
            changeName(product.getName());
        }

        if (!ObjectUtils.isEmpty(product.getPrice())) {
            changePrice(product.getPrice());
        }

        changeAmount(product.getAmount());
        lastModifiedAt = LocalDateTime.now();
    }

    public Product toDomain() {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .amount(amount)
                .createdAt(createdAt)
                .lastModifiedAt(lastModifiedAt)
                .build();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            log.error("[ProductEntity.changePrice] price invalid {}", price);
            throw new IllegalArgumentException("유효하지 않은 가격입니다. price=" + price);  // TODO 구체적인 에러 발생
        }

        this.price = price;
    }

    public void changeAmount(int amount) {
        if (amount < 0) {
            log.error("[ProductEntity.changeAmount] amount invalid {}", amount);
            throw new IllegalArgumentException("수량은 0 또는 양수여야 합니다. amount=" + amount);  // TODO 구체적인 에러 발생
        }

        this.amount = amount;
    }
}
