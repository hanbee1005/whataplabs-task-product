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
import java.util.concurrent.*;

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

    @Test
    @DisplayName("하나의 상품에 대해 동시에 재고 차감 요청이 오는 경우 하나만 성공하고 다른 요청은 실패합니다.")
    public void deductStockConcurrent() throws InterruptedException {
        Long productId = products.get(1);

        int numberOfThreads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Callable<Integer> task = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                OrderedProduct orderedProduct = OrderedProduct.builder()
                        .productId(productId)
                        .quantity(1)
                        .unitPrice(BigDecimal.valueOf(2000))
                        .build();
                return repository.deduct(orderedProduct);
            } catch (Exception e) {
                return 0;
            } finally {
                doneLatch.countDown();
            }
        };

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            futures.add(executor.submit(task));
        }

        latch.countDown(); // Let all threads proceed
        doneLatch.await(); // Wait for all threads to finish

        int successCount = 0;
        int failureCount = 0;

        for (Future<Integer> future : futures) {
            try {
                if (future.get() == 1) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (ExecutionException e) {
                failureCount++;
            }
        }

        assertThat(successCount).isEqualTo(1);
        assertThat(failureCount).isEqualTo(numberOfThreads - 1);
    }

    private List<Product> testProducts() {
        return List.of(
                Product.builder().name("test item 1").price(BigDecimal.valueOf(1000)).amount(5).createdAt(LocalDateTime.now()).build(),
                Product.builder().name("test item 2").price(BigDecimal.valueOf(2000)).amount(3).createdAt(LocalDateTime.now()).build(),
                Product.builder().name("test item 3").price(BigDecimal.valueOf(1500)).amount(7).createdAt(LocalDateTime.now()).build()
        );
    }

}