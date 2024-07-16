package com.whataplabs.task.product.whataplabstaskproduct.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum SortType {
    NAME("name"),
    PRICE("price"),
    AMOUNT("amount"),
    ;

    private final String property;

    public static SortType of(String property) {
        for (SortType type : SortType.values()) {
            if (type.property.equals(property)) {
                return type;
            }
        }

        throw new IllegalArgumentException("유효하지 않은 정렬 조건입니다. property={}" + property);
    }

    public Sort.Order getOrder() {
        return switch (this) {
            case NAME -> Sort.Order.asc(NAME.getProperty());
            case PRICE -> Sort.Order.desc(PRICE.getProperty());
            case AMOUNT -> Sort.Order.desc(AMOUNT.getProperty());
        };
    }
}
