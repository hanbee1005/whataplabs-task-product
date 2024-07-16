package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web;

import com.whataplabs.task.product.whataplabstaskproduct.application.service.ProductService;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.AddProductRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.GetProductsRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.request.UpdateProductRequest;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.AddProductResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.GetProductResponse;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.GetProductsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductRestController {
    private final ProductService service;

    @GetMapping("/products/{id}")
    public ResponseEntity<GetProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(GetProductResponse.from(service.getProduct(id)));
    }

    @GetMapping("/products")
    public ResponseEntity<GetProductsResponse> getProducts(@Valid GetProductsRequest request) {
        return ResponseEntity.ok(GetProductsResponse.from(
                service.getProductsByPagination(request.page(), request.size(), request.sortType())
        ));
    }

    @PostMapping("/products")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody @Valid AddProductRequest request) {
        return ResponseEntity.ok(AddProductResponse.from(service.addProduct(request.to())));
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Long id,
                                                @RequestBody @Valid UpdateProductRequest request) {
        service.updateProduct(request.to(id));
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        service.deleteProduct(id);
        return ResponseEntity.ok("ok");
    }
}
