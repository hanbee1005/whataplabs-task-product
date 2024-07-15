package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
