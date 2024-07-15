package com.whataplabs.task.product.whataplabstaskproduct.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRepositoryTest {

    private ProductRepository repository = new FakeProductRepository();

    @Test
    @DisplayName("상품 id로 조회하기")
    public void getProduct() {
        // given
        Long id = 1L;

        // when
        Product product = repository.getProduct(id).orElse(null);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("상품 추가하기")
    public void addProduct() {
        // given
        Product newProduct = Product.builder().name("item6").price(BigDecimal.valueOf(15000)).amount(7).createdAt(LocalDateTime.now()).build();

        // when
        Product product = repository.addProduct(newProduct);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(6L);
        assertThat(product.getName()).isEqualTo("item6");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(15000));
        assertThat(product.getAmount()).isEqualTo(7);
    }

    @Test
    @DisplayName("상품 수정하기")
    public void updateProduct() {
        // given
        Product product = Product.builder()
                .id(3L)
                .name("item33")
                .price(BigDecimal.valueOf(7300))
                .amount(5)
                .lastModifiedAt(LocalDateTime.now())
                .build();

        // when
        int result = repository.updateProduct(product);
        assertThat(result).isEqualTo(1);

        Product updated = repository.getProduct(3L).orElse(null);

        // then
        assertThat(updated).isNotNull();
        assertThat(product.getId()).isEqualTo(3L);
        assertThat(product.getName()).isEqualTo("item33");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(7300));
        assertThat(product.getAmount()).isEqualTo(5);
    }

    @Test
    @DisplayName("상품 삭제하기")
    public void deleteProduct() {
        // given
        Long id = 2L;

        // when
        int result = repository.deleteProduct(id);
        assertThat(result).isEqualTo(1);

        // then
        assertThat(repository.getProduct(id)).isEmpty();
    }

}