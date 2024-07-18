package com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.exception;

import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.InsufficientStockException;
import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ProductBusinessException;
import com.whataplabs.task.product.whataplabstaskproduct.interfaces.web.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<CommonResponse<CommonResponse<Object>>> handleInsufficientStockException(InsufficientStockException ex) {
        log.error("[GlobalExceptionHandler.handleInsufficientStockException] message={}", ex.getErrorMessage());
        return ResponseEntity.status(400).body(CommonResponse.of(ex));
    }

    @ExceptionHandler(ProductBusinessException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleProductException(ProductBusinessException ex) {
        log.error("[GlobalExceptionHandler.handleProductException] message={}", ex.getErrorMessage());
        return ResponseEntity.status(400).body(CommonResponse.of(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleException(Exception ex) {
        log.error("[GlobalExceptionHandler.handleException] message={}", ex.getMessage());
        return ResponseEntity.status(500).body(CommonResponse.of(ErrorResponse.createInternalServerError()));
    }
}
