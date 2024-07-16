package com.whataplabs.task.product.whataplabstaskproduct.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FakeProductRepository implements ProductRepository {
    private final Map<Long, Product> store = new ConcurrentHashMap<>();

    public FakeProductRepository() {
        for (int i = 1; i <= 5; i++) {
            store.put((long) i, Product.builder()
                    .id((long)i)
                    .name("item" + i)
                    .price(BigDecimal.valueOf(i * 10000))
                    .amount(i * 100)
                    .createdAt(LocalDateTime.now())
                    .build()
            );
        }
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Page<Product> getProductsByPagination(Pageable pageable) {
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        Product newProduct = Product.builder()
                .id((long)(store.size()) + 1)
                .name(product.getName())
                .price(product.getPrice())
                .amount(product.getAmount())
                .createdAt(product.getCreatedAt())
                .build();
        store.put(newProduct.getId(), newProduct);
        return newProduct;
    }

    @Override
    public int updateProduct(Product product) {
        if (!store.containsKey(product.getId())) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + product.getId());
        }

        Product updated = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .amount(product.getAmount())
                .lastModifiedAt(product.getLastModifiedAt())
                .build();

        store.put(product.getId(), updated);
        return 1;
    }

    @Override
    public int deleteProduct(Long id) {
        if (!store.containsKey(id)) {
            throw new IllegalArgumentException("해당 상품이 존재하지 않습니다. id=" + id);
        }

        Product removed = store.remove(id);
        return removed != null ? 1 : 0;
    }
}
