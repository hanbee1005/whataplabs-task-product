package com.whataplabs.task.product.whataplabstaskproduct.application.service;

import com.whataplabs.task.product.whataplabstaskproduct.domain.*;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return repository.getProduct(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public ProductWithPageInfo getProductsByPagination(int page, int size, SortType sortType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortType.getOrder()));
        return ProductWithPageInfo.of(repository.getProductsByPagination(pageable));
    }

    @Transactional
    public Product addProduct(Product newProduct) {
        return repository.addProduct(newProduct);
    }

    @Transactional
    public void updateProduct(Product updateProduct) {
        int affected = repository.updateProduct(updateProduct);
        if (affected == 0) {
            throw new IllegalStateException("product update fail");
        }
    }

    @Transactional
    public void deleteProduct(Long id) {
        int affected = repository.deleteProduct(id);
        if (affected == 0) {
            throw new IllegalStateException("product delete fail");
        }
    }
}
