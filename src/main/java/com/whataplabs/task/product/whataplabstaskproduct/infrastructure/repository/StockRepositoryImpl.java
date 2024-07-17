package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;
import com.whataplabs.task.product.whataplabstaskproduct.domain.StockRepository;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StockRepositoryImpl implements StockRepository {
    private final StockJpaRepository jpaRepository;

    @Override
    @Transactional
    public int deduct(OrderedProduct product) {
        ProductEntity entity = jpaRepository.findById(product.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(product.getProductId()));
        entity.deductAmount(product.getQuantity());
        return 1;
    }
}
