package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whataplabs.task.product.whataplabstaskproduct.application.service.ProductService;
import com.whataplabs.task.product.whataplabstaskproduct.domain.Product;
import com.whataplabs.task.product.whataplabstaskproduct.domain.ProductWithPageInfo;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ProductNotFoundException;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.AddProductRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.GetProductsRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.UpdateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductService service;

    @Test
    @DisplayName("id로 상품 조회")
    public void getProduct() throws Exception {
        // given
        Long id = 1L;
        Product product = Product.builder()
                .id(id).name("test item 1").price(BigDecimal.valueOf(1200)).amount(3)
                .build();
        given(service.getProduct(anyLong())).willReturn(product);

        // when
        // then
        mockMvc.perform(get("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.name").value("test item 1"))
                .andExpect(jsonPath("$.data.price").value(1200))
                .andExpect(jsonPath("$.data.amount").value(3))
        ;
    }

    @Test
    @DisplayName("id로 상품 조회 시 id 가 없는 경우 에러")
    public void getProductFail() throws Exception {
        // given
        Long id = -1L;
        given(service.getProduct(anyLong())).willThrow(ProductNotFoundException.class);

        // when
        // then
        mockMvc.perform(get("/products/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("NOT_FOUND_PRODUCT"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품입니다."))
        ;
    }

    @Test
    @DisplayName("상품 목록 조회")
    public void getProducts() throws Exception {
        // given
        GetProductsRequest request = new GetProductsRequest(0, 3, "price");
        ProductWithPageInfo response = ProductWithPageInfo.builder()
                .totalPages(2)
                .page(0)
                .size(3)
                .products(List.of(
                        Product.builder().id(1L).name("test item 1").price(BigDecimal.valueOf(1200)).amount(3).build(),
                        Product.builder().id(2L).name("test item 2").price(BigDecimal.valueOf(2300)).amount(5).build(),
                        Product.builder().id(3L).name("test item 3").price(BigDecimal.valueOf(4300)).amount(7).build()
                ))
                .build();
        given(service.getProductsByPagination(anyInt(), anyInt(), any())).willReturn(response);

        // when
        // then
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPages").value(2))
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(3))
        ;
    }

    @Test
    @DisplayName("상품 등록")
    public void addProduct() throws Exception {
        // given
        AddProductRequest request = new AddProductRequest("test item 22", BigDecimal.valueOf(1200), 3);
        Product product = Product.builder()
                .id(22L).name("test item 22").price(BigDecimal.valueOf(1200)).amount(3)
                .build();
        given(service.addProduct(any())).willReturn(product);

        // when
        // then
        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(22))
                .andExpect(jsonPath("$.data.name").value("test item 22"))
                .andExpect(jsonPath("$.data.price").value(1200))
                .andExpect(jsonPath("$.data.amount").value(3))
        ;
    }

    @Test
    @DisplayName("상품 수정")
    public void updateProduct() throws Exception {
        // given
        UpdateProductRequest request = new UpdateProductRequest("test item 11", BigDecimal.valueOf(1300), 7);
        doNothing().when(service).updateProduct(any());

        // when
        // then
        mockMvc.perform(patch("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1))
        ;
    }

    @Test
    @DisplayName("상품 삭제")
    public void deleteProduct() throws Exception {
        // given
        doNothing().when(service).deleteProduct(anyLong());

        // when
        // then
        mockMvc.perform(delete("/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1))
        ;
    }

}