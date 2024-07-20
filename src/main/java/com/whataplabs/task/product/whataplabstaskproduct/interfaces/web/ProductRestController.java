package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web;

import com.whataplabs.task.product.whataplabstaskproduct.application.service.ProductService;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.AddProductRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.GetProductsRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.UpdateProductRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.AddProductResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.CommonResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.GetProductResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.GetProductsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Tag(name = "상품 API", description = "상품 조회, 등록, 수정 삭제 API")
@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService service;

    @Operation(summary = "상품 조회 (by. id)", description = "상품 id로 상품 정보를 조회합니다.")
    @GetMapping("/products/{id}")
    public ResponseEntity<CommonResponse<GetProductResponse>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(CommonResponse.ok(GetProductResponse.from(service.getProduct(id))));
    }

    @Operation(summary = "상품 목록 조회 (with. paging)", description = "page, size, sort 항목을 받아 페이징을 적용한 상품 목록을 조회합니다.")
    @GetMapping("/products")
    public ResponseEntity<CommonResponse<GetProductsResponse>> getProducts(@Valid GetProductsRequest request) {
        return ResponseEntity.ok(CommonResponse.ok(
                GetProductsResponse.from(service.getProductsByPagination(request.page(), request.size(), request.sortType())
        )));
    }

    @Operation(summary = "상품 등록 (단건)", description = "상품 정보를 입력 받아 상품을 등록합니다.")
    @PostMapping("/products")
    public ResponseEntity<CommonResponse<AddProductResponse>> addProduct(@RequestBody @Valid AddProductRequest request) {
        return ResponseEntity.ok(CommonResponse.ok(AddProductResponse.from(service.addProduct(request.to()))));
    }

    @Operation(summary = "상품 수정", description = "id와 상품 정보를 입력 받아 상품을 수정합니다.")
    @PutMapping("/products/{id}")
    public ResponseEntity<CommonResponse<Object>> updateProduct(@PathVariable Long id,
                                                @RequestBody @Valid UpdateProductRequest request) {
        service.updateProduct(request.to(id));
        return ResponseEntity.ok(CommonResponse.ok(id));
    }

    @Operation(summary = "상품 삭제", description = "id 입력 받아 상품을 삭제합니다.")
    @DeleteMapping("/products/{id}")
    public ResponseEntity<CommonResponse<Object>> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok(CommonResponse.ok(id));
    }
}
