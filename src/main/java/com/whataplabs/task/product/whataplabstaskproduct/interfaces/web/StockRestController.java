package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web;

import com.whataplabs.task.product.whataplabstaskproduct.application.service.StockManager;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.ProductOrderRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.CommonResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.ProductOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StockRestController {
    private final StockManager stockManager;

    @PostMapping("/products/order")
    public ResponseEntity<CommonResponse<ProductOrderResponse>> productOrder(@RequestBody @Valid ProductOrderRequest request) {
        List<Long> deducted = stockManager.deductStock(request.orderedProducts());
        return ResponseEntity.ok(CommonResponse.ok(new ProductOrderResponse(deducted)));
    }
}
