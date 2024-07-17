package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;
import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class StockRepositoryImplTest {

    @Autowired StockRepositoryImpl repository;
    @Autowired ProductJpaRepository productJpaRepository;

    private List<Long> products = new ArrayList<>();

    @BeforeEach
    void init() {
        testProducts().forEach(product -> {
            ProductEntity entity = ProductEntity.create(product);
            productJpaRepository.save(entity);
            products.add(entity.getId());
        });
    }

    @AfterEach
    void destroy() {
        productJpaRepository.deleteAllById(products);
    }

    @Test
    @DisplayName("재고 차감 성공")
    public void deductStock() {
        // given
        Long productId = products.get(0);
        OrderedProduct orderedProduct = OrderedProduct.builder()
                .productId(productId)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(1000))
                .build();

        ProductEntity before = productJpaRepository.findById(productId).get();

        // when
        int affected = repository.deduct(orderedProduct);
        ProductEntity deducted = productJpaRepository.findById(productId).orElse(null);

        // then
        assertThat(affected).isEqualTo(1);
        assertThat(deducted).isNotNull();
        assertThat(deducted.getAmount()).isEqualTo(before.getAmount() - 2);
    }

    @Test
    @DisplayName("재고 차감 실패")
    public void deductStockFail() {
        // given
        Long productId = products.get(0);
        OrderedProduct orderedProduct = OrderedProduct.builder()
                .productId(productId)
                .quantity(7)
                .unitPrice(BigDecimal.valueOf(1000))
                .build();

        // when
        // then
        assertThrows(InsufficientStockException.class, () -> repository.deduct(orderedProduct),
                "상품의 재고가 충분하지 않습니다. id=" + productId);
    }

    private List<Product> testProducts() {
        return List.of(
                Product.builder().name("test item 1").price(BigDecimal.valueOf(1000)).amount(5).createdAt(LocalDateTime.now()).build(),
                Product.builder().name("test item 2").price(BigDecimal.valueOf(2000)).amount(3).createdAt(LocalDateTime.now()).build(),
                Product.builder().name("test item 3").price(BigDecimal.valueOf(1500)).amount(7).createdAt(LocalDateTime.now()).build()
        );
    }

}