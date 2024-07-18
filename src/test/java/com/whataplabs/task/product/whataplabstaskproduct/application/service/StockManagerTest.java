package com.whataplabs.task.product.whataplabstaskproduct.application.service;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;
import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.InsufficientStockException;
import com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository.ProductEntity;
import com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository.ProductJpaRepository;
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
class StockManagerTest {

    @Autowired StockManager stockManager;

    @Autowired
    ProductJpaRepository productJpaRepository;

    private List<ProductEntity> products = new ArrayList<>();

    @BeforeEach
    void init() {
        testProducts().forEach(product -> {
            ProductEntity entity = ProductEntity.create(product);
            productJpaRepository.save(entity);
            products.add(entity);
        });
    }

    @AfterEach
    void destroy() {
        productJpaRepository.deleteAllById(products.stream().map(ProductEntity::getId).toList());
    }

    @Test
    @DisplayName("상품 다건 재고 차감 성공")
    public void deductStocks() {
        // given
        List<OrderedProduct> orderedProducts = List.of(
                OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build(),
                OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build(),
                OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
        );

        // when
        List<Long> deducted = stockManager.deductStock(orderedProducts);

        // then
        for (int i = 0; i < deducted.size(); i++) {
            ProductEntity entity = productJpaRepository.findById(deducted.get(i)).orElse(null);
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(products.get(i).getId());
            assertThat(entity.getAmount()).isEqualTo(products.get(i).getAmount() - orderedProducts.get(i).getQuantity());
        }
    }

    @Test
    @DisplayName("상품 다건 재고 차감 실패")
    public void deductStocksFail() {
        // given
        List<OrderedProduct> orderedProducts = List.of(
                OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build(),
                OrderedProduct.builder().productId(products.get(1).getId()).quantity(8).unitPrice(BigDecimal.valueOf(2000)).build(),
                OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
        );

        // when
        // then
        assertThrows(InsufficientStockException.class, () -> stockManager.deductStock(orderedProducts),
                "상품의 재고가 충분하지 않습니다. id=" + products.get(1).getId());
    }

    @Test
    @DisplayName("서로 다른 상품에 대한 재고 차감 요청은 모두 성공합니다.")
    public void deductStockConcurrent() throws InterruptedException {
        int numberOfThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Callable<List<Long>> task1 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build()
                );
                return stockManager.deductStock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        Callable<List<Long>> task2 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build(),
                        OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
                );
                return stockManager.deductStock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        List<Future<List<Long>>> futures = new ArrayList<>();
        futures.add(executor.submit(task1));
        futures.add(executor.submit(task2));

        latch.countDown(); // Let all threads proceed
        doneLatch.await(); // Wait for all threads to finish

        int successCount = 0;
        int failureCount = 0;

        for (Future<List<Long>> future : futures) {
            try {
                if (future.get() != null) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (ExecutionException e) {
                failureCount++;
            }
        }

        assertThat(successCount).isEqualTo(2);
        assertThat(failureCount).isEqualTo(0);
    }

    @Test
    @DisplayName("동일한 상품이 하나라도 포함된 경우 하나의 요청만 성공하고 다른 요청은 실패합니다.")
    public void deductStockConcurrent2() throws InterruptedException {
        int numberOfThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Callable<List<Long>> task1 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build(),
                        OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build()
                );
                return stockManager.deductStock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        Callable<List<Long>> task2 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build(),
                        OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
                );
                return stockManager.deductStock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        List<Future<List<Long>>> futures = new ArrayList<>();
        futures.add(executor.submit(task1));
        futures.add(executor.submit(task2));

        latch.countDown(); // Let all threads proceed
        doneLatch.await(); // Wait for all threads to finish

        int successCount = 0;
        int failureCount = 0;

        for (Future<List<Long>> future : futures) {
            try {
                if (future.get() != null) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (ExecutionException e) {
                failureCount++;
            }
        }

        assertThat(successCount).isEqualTo(1);
        assertThat(failureCount).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 다건 재고 롤백 성공")
    public void restocks() {
        // given
        List<OrderedProduct> orderedProducts = List.of(
                OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build(),
                OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build(),
                OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
        );

        // when
        List<Long> deducted = stockManager.restock(orderedProducts);

        // then
        for (int i = 0; i < deducted.size(); i++) {
            ProductEntity entity = productJpaRepository.findById(deducted.get(i)).orElse(null);
            assertThat(entity).isNotNull();
            assertThat(entity.getId()).isEqualTo(products.get(i).getId());
            assertThat(entity.getAmount()).isEqualTo(products.get(i).getAmount() + orderedProducts.get(i).getQuantity());
        }
    }

    @Test
    @DisplayName("상품 다건 재고 롤백 실패")
    public void restocksFail() {
        // given
        List<OrderedProduct> orderedProducts = List.of(
                OrderedProduct.builder().productId(products.get(0).getId()).quantity(-300).unitPrice(BigDecimal.valueOf(1000)).build(),
                OrderedProduct.builder().productId(products.get(1).getId()).quantity(8).unitPrice(BigDecimal.valueOf(2000)).build(),
                OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
        );

        // when
        // then
        assertThrows(InsufficientStockException.class, () -> stockManager.restock(orderedProducts),
                "상품의 재고가 충분하지 않습니다. id=" + products.get(1).getId());
    }

    @Test
    @DisplayName("서로 다른 상품에 대한 재고 롤백 요청은 모두 성공합니다.")
    public void restockConcurrent() throws InterruptedException {
        int numberOfThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Callable<List<Long>> task1 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build()
                );
                return stockManager.restock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        Callable<List<Long>> task2 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build(),
                        OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
                );
                return stockManager.restock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        List<Future<List<Long>>> futures = new ArrayList<>();
        futures.add(executor.submit(task1));
        futures.add(executor.submit(task2));

        latch.countDown(); // Let all threads proceed
        doneLatch.await(); // Wait for all threads to finish

        int successCount = 0;
        int failureCount = 0;

        for (Future<List<Long>> future : futures) {
            try {
                if (future.get() != null) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (ExecutionException e) {
                failureCount++;
            }
        }

        assertThat(successCount).isEqualTo(2);
        assertThat(failureCount).isEqualTo(0);
    }

    @Test
    @DisplayName("동일한 상품이 하나라도 포함된 경우 재고 롤백에 대한 하나의 요청만 성공하고 다른 요청은 실패합니다.")
    public void restockConcurrent2() throws InterruptedException {
        int numberOfThreads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(numberOfThreads);

        Callable<List<Long>> task1 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(0).getId()).quantity(3).unitPrice(BigDecimal.valueOf(1000)).build(),
                        OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build()
                );
                return stockManager.restock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        Callable<List<Long>> task2 = () -> {
            try {
                latch.await(); // Wait until all threads are ready
                List<OrderedProduct> orderedProducts = List.of(
                        OrderedProduct.builder().productId(products.get(1).getId()).quantity(1).unitPrice(BigDecimal.valueOf(2000)).build(),
                        OrderedProduct.builder().productId(products.get(2).getId()).quantity(4).unitPrice(BigDecimal.valueOf(1500)).build()
                );
                return stockManager.restock(orderedProducts);
            } catch (Exception e) {
                return null;
            } finally {
                doneLatch.countDown();
            }
        };

        List<Future<List<Long>>> futures = new ArrayList<>();
        futures.add(executor.submit(task1));
        futures.add(executor.submit(task2));

        latch.countDown(); // Let all threads proceed
        doneLatch.await(); // Wait for all threads to finish

        int successCount = 0;
        int failureCount = 0;

        for (Future<List<Long>> future : futures) {
            try {
                if (future.get() != null) {
                    successCount++;
                } else {
                    failureCount++;
                }
            } catch (ExecutionException e) {
                failureCount++;
            }
        }

        assertThat(successCount).isEqualTo(1);
        assertThat(failureCount).isEqualTo(1);
    }

    private List<Product> testProducts() {
        return List.of(
                Product.builder().name("test item 1").price(BigDecimal.valueOf(1000)).amount(5).createdAt(LocalDateTime.now()).build(),
                Product.builder().name("test item 2").price(BigDecimal.valueOf(2000)).amount(3).createdAt(LocalDateTime.now()).build(),
                Product.builder().name("test item 3").price(BigDecimal.valueOf(1500)).amount(7).createdAt(LocalDateTime.now()).build()
        );
    }

}