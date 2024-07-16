package com.whataplabs.task.product.whataplabstaskproduct.domain;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductWithPageInfo {
    private int totalPages;
    private int page;
    private int size;
    private List<Product> products;

    public static ProductWithPageInfo of(Page<Product> productPage) {
        return ProductWithPageInfo.builder()
                .totalPages(productPage.getTotalPages())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .products(productPage.get().toList())
                .build();
    }
}
