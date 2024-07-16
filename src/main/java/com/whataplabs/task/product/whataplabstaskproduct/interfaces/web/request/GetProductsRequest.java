package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.SortType;
import org.apache.commons.lang3.ObjectUtils;

public record GetProductsRequest(
        Integer page,
        Integer size,
        String sort
) {
    public GetProductsRequest {
        if (ObjectUtils.isEmpty(page)) {
            page = 0;
        }

        if (ObjectUtils.isEmpty(size)) {
            size = 5;
        }

        if (ObjectUtils.isEmpty(sort)) {
            sort = "name";
        }
    }

    public SortType sortType() {
        return SortType.of(sort);
    }
}
