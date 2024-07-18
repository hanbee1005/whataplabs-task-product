package com.whataplabs.task.product.whataplabstaskproduct.application.service;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;
import com.whataplabs.task.product.whataplabstaskproduct.domain.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockManager {
    private final StockRepository repository;

    @Transactional
    public List<Long> deductStock(List<OrderedProduct> products) {
        List<Long> successIds = new ArrayList<>();
        for (OrderedProduct product : products) {
            int affected = repository.deduct(product);
            if (affected == 0) {
                throw new IllegalStateException("product deduct stock fail");
            }

            successIds.add(product.getProductId());
        }

        return successIds;
    }

    @Transactional
    public List<Long> restock(List<OrderedProduct> products) {
        List<Long> successIds = new ArrayList<>();
        for (OrderedProduct product : products) {
            int affected = repository.restock(product);
            if (affected == 0) {
                throw new IllegalStateException("product restock fail");
            }

            successIds.add(product.getProductId());
        }

        return successIds;
    }
}
