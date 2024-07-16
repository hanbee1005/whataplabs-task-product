package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.repository;

import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import com.whataplabs.task.product.whataplabstaskproduct.domain.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProductRepositoryImplTest {
    @Autowired ProductRepositoryImpl repository;

    @Test
    @DisplayName("id로 상품 조회")
    public void getProduct() {
        // given
        Long id = 101L;

        // when
        Optional<Product> product = repository.getProduct(id);

        // then
        assertThat(product).isNotEmpty();
    }

    @Test
    @DisplayName("id로 상품 조회 실패 - 존재하지 않는 id")
    public void getProductFail() {
        // given
        Long id = -1L;

        // when
        // then
        assertThat(repository.getProduct(id)).isEmpty();
    }

    @Test
    @DisplayName("상품 조회 (with. paging)")
    public void getProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 3);

        // when
        Page<Product> products = repository.getProductsByPagination(pageable);

        // then
        assertThat(products).isNotNull();
        assertThat(products.getTotalPages()).isGreaterThanOrEqualTo(2);
        assertThat(products.getTotalElements()).isGreaterThanOrEqualTo(4);
        assertThat(products.getSize()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 등록")
    public void addProduct() {
        // given
        Product newProduct = Product.builder().name("test item 1").price(BigDecimal.valueOf(15500)).amount(7).createdAt(LocalDateTime.now()).build();

        // when
        Product saved = repository.addProduct(newProduct);
        Optional<Product> product = repository.getProduct(saved.getId());

        // then
        assertThat(saved).isNotNull();
        assertThat(product).isNotEmpty();
        assertThat(product.get().getName()).isEqualTo(newProduct.getName());
        assertThat(product.get().getPrice().intValue()).isEqualTo(newProduct.getPrice().intValue());
        assertThat(product.get().getAmount()).isEqualTo(newProduct.getAmount());
    }

    @Test
    @DisplayName("상품 수정")
    public void updateProduct() {
        // given
        Product updateProduct = Product.builder()
                .id(102L)
                .name("test item 2")
                .price(BigDecimal.valueOf(30500))
                .amount(19)
                .lastModifiedAt(LocalDateTime.now())
                .build();

        // when
        int result = repository.updateProduct(updateProduct);
        assertThat(result).isEqualTo(1);

        Optional<Product> product = repository.getProduct(updateProduct.getId());

        // then
        assertThat(product).isNotEmpty();
        assertThat(product.get().getName()).isEqualTo(updateProduct.getName());
        assertThat(product.get().getPrice().intValue()).isEqualTo(updateProduct.getPrice().intValue());
        assertThat(product.get().getAmount()).isEqualTo(updateProduct.getAmount());
    }

    @Test
    @DisplayName("존재하지 않는 id의 상품 업데이트 시 실패")
    public void updateProductFail() {
        // given
        Product updateProduct = Product.builder().id(-1L).build();

        // when
        // then
        assertThrows(ProductNotFoundException.class, () -> repository.updateProduct(updateProduct), "존재하지 않는 상품입니다. id=" + -1);
    }

    @Test
    @DisplayName("상품 삭제")
    public void deleteProduct() {
        // given
        Long id = 103L;

        // when
        int result = repository.deleteProduct(id);
        assertThat(result).isEqualTo(1);

        // then
        assertThat(repository.getProduct(id)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 id의 상품 삭제 시 실패")
    public void deleteProductFail() {
        // given
        Long id = -1L;

        // when
        // then
        assertThrows(ProductNotFoundException.class, () -> repository.deleteProduct(id), "해당 상품이 존재하지 않습니다. id=" + id);
    }
}