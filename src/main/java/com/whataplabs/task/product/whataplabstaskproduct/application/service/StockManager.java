package com.whataplabs.task.product.whataplabstaskproduct.application.service;

import com.whataplabs.task.product.whataplabstaskproduct.domain.OrderedProduct;
import com.whataplabs.task.product.whataplabstaskproduct.domain.StockRepository;
import com.whataplabs.task.product.whataplabstaskproduct.infrastructure.handler.LockHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockManager {
    public static String STOCK_LOCK_PREFIX = "PRODUCT_STOCK_";

    private final StockRepository repository;
    private final LockHandler lockHandler;

    public List<Long> deductStock(List<OrderedProduct> products) {
        List<Long> successIds = new ArrayList<>();
        for (OrderedProduct product : products) {
            Long id = lockHandler.runOnLock(STOCK_LOCK_PREFIX + product.getProductId(), 2000L, 1000L, () -> {
                int affected = repository.deduct(product);
                if (affected == 0) {
                    throw new IllegalStateException("product deduct stock fail");
                }

                return product.getProductId();
            });

            successIds.add(id);
        }

        return successIds;
    }

    public List<Long> restock(List<OrderedProduct> products) {
        List<Long> successIds = new ArrayList<>();
        for (OrderedProduct product : products) {
            Long id = lockHandler.runOnLock(STOCK_LOCK_PREFIX + product.getProductId(), 2000L, 1000L, () -> {
                int affected = repository.restock(product);
                if (affected == 0) {
                    throw new IllegalStateException("product restock fail");
                }
                return product.getProductId();
            });

            successIds.add(id);
        }

        return successIds;
    }
}
