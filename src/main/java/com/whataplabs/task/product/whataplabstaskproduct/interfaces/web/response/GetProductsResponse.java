package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response;

import com.whataplabs.task.product.whataplabstaskproduct.domain.ProductWithPageInfo;

import java.util.List;

public record GetProductsResponse(
        int totalPages,
        int page,
        int size,
        List<GetProductResponse> products
) {
    public static GetProductsResponse from(ProductWithPageInfo productWithPageInfo) {
        return new GetProductsResponse(productWithPageInfo.getTotalPages(), productWithPageInfo.getPage(), productWithPageInfo.getSize(),
                productWithPageInfo.getProducts().stream().map(GetProductResponse::from).toList());
    }
}
