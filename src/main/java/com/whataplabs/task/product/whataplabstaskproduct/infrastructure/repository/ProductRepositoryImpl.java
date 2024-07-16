package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import com.whataplabs.task.product.whataplabstaskproduct.domain.ProductNotFoundException;
import com.whataplabs.task.product.whataplabstaskproduct.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    @Override
    public Optional<Product> getProduct(Long id) {
        return jpaRepository.findById(id).map(ProductEntity::toDomain);
    }

    @Override
    public Page<Product> getProductsByPagination(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(ProductEntity::toDomain);
    }

    @Override
    public Product addProduct(Product product) {
        ProductEntity entity = ProductEntity.create(product);
        jpaRepository.save(entity);
        return entity.toDomain();
    }

    @Override
    @Transactional
    public int updateProduct(Product product) {
        ProductEntity entity = jpaRepository.findById(product.getId())
                .orElseThrow(() -> new ProductNotFoundException(product.getId()));

        entity.update(product);
        return 1;
    }

    @Override
    public int deleteProduct(Long id) {
        ProductEntity entity = jpaRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        jpaRepository.delete(entity);
        return 1;
    }
}
