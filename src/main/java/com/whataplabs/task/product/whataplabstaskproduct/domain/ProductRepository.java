package com.whataplabs.task.product.whataplabstaskproduct.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> getProduct(Long id);
    Page<Product> getProductsByPagination(Pageable pageable);
    Product addProduct(Product product);
    int updateProduct(Product product);
    int deleteProduct(Long id);
}
