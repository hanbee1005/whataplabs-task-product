package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web;

import com.whataplabs.task.product.whataplabstaskproduct.application.service.StockManager;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.ProductOrderRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.CommonResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.ProductOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "재고 API", description = "재고 추가 및 차감 API")
@RestController
@RequiredArgsConstructor
public class StockRestController {
    private final StockManager stockManager;

    @Operation(summary = "재고 차감", description = "요청한 상품의 재고를 차감합니다.")
    @PostMapping("/products/stock/deduct")
    public ResponseEntity<CommonResponse<ProductOrderResponse>> productOrder(@RequestBody @Valid ProductOrderRequest request) {
        List<Long> deducted = stockManager.deductStock(request.orderedProducts());
        return ResponseEntity.ok(CommonResponse.ok(new ProductOrderResponse(deducted)));
    }

    @Operation(summary = "재고 추가", description = "요청한 상품의 재고를 추가합니다.")
    @PostMapping("/products/stock/add")
    public ResponseEntity<CommonResponse<ProductOrderResponse>> productOrderCancel(@RequestBody @Valid ProductOrderRequest request) {
        List<Long> restocked = stockManager.restock(request.orderedProducts());
        return ResponseEntity.ok(CommonResponse.ok(new ProductOrderResponse(restocked)));
    }
}
