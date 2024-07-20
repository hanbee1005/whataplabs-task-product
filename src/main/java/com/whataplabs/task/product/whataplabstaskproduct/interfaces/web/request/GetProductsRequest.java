package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request;

import com.whataplabs.task.product.whataplabstaskproduct.domain.SortType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.lang3.ObjectUtils;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public record GetProductsRequest(
        @Schema(description = "page 번호", example = "0")
        @PositiveOrZero
        Integer page,
        @Schema(description = "조회 개수", example = "3")
        @Positive
        Integer size,
        @Schema(description = "정렬 조건", example = "price")
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
