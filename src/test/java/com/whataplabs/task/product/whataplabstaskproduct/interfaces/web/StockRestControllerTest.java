package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whataplabs.task.product.whataplabstaskproduct.application.service.StockManager;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.InsufficientStockException;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.OrderedProductRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.ProductOrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockRestController.class)
class StockRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StockManager stockManager;

    @Test
    @DisplayName("재고 확인 및 차감")
    public void deductStock() throws Exception {
        // given
        ProductOrderRequest request = new ProductOrderRequest(List.of(
                new OrderedProductRequest(1L, 5, BigDecimal.valueOf(1000)),
                new OrderedProductRequest(2L, 1, BigDecimal.valueOf(2000)),
                new OrderedProductRequest(3L, 4, BigDecimal.valueOf(1500))
        ));
        given(stockManager.deductStock(any())).willReturn(List.of(1L, 2L, 3L));

        // when
        // then
        MvcResult result = mockMvc.perform(post("/products/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"products\":[1,2,3]"));
    }

    @Test
    @DisplayName("재고 확인 및 차감 실패")
    public void deductStockFail() throws Exception {
        // given
        ProductOrderRequest request = new ProductOrderRequest(List.of(
                new OrderedProductRequest(1L, 5, BigDecimal.valueOf(1000)),
                new OrderedProductRequest(2L, 8, BigDecimal.valueOf(2000)),
                new OrderedProductRequest(3L, 4, BigDecimal.valueOf(1500))
        ));
        given(stockManager.deductStock(any())).willThrow(InsufficientStockException.class);

        // when
        // then
        mockMvc.perform(post("/products/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_STOCK"))
                .andExpect(jsonPath("$.message").value("상품의 재고가 충분하지 않습니다."))
                ;
    }

    @Test
    @DisplayName("재고 롤백 성공")
    public void restock() throws Exception {
        // given
        ProductOrderRequest request = new ProductOrderRequest(List.of(
                new OrderedProductRequest(1L, 5, BigDecimal.valueOf(1000)),
                new OrderedProductRequest(2L, 1, BigDecimal.valueOf(2000)),
                new OrderedProductRequest(3L, 4, BigDecimal.valueOf(1500))
        ));
        given(stockManager.restock(any())).willReturn(List.of(1L, 2L, 3L));

        // when
        // then
        MvcResult result = mockMvc.perform(post("/products/order/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("\"products\":[1,2,3]"));
    }

    @Test
    @DisplayName("재고 롤백 실패")
    public void restockFail() throws Exception {
        // given
        ProductOrderRequest request = new ProductOrderRequest(List.of(
                new OrderedProductRequest(1L, -15, BigDecimal.valueOf(1000)),
                new OrderedProductRequest(2L, 8, BigDecimal.valueOf(2000)),
                new OrderedProductRequest(3L, 4, BigDecimal.valueOf(1500))
        ));
        given(stockManager.restock(any())).willThrow(InsufficientStockException.class);

        // when
        // then
        mockMvc.perform(post("/products/order/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_STOCK"))
                .andExpect(jsonPath("$.message").value("상품의 재고가 충분하지 않습니다."))
        ;
    }
}