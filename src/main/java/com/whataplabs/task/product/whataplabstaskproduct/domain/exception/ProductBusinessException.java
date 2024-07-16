package com.whataplabs.task.product.whataplabstaskproduct.domain.exception;

public abstract class ProductBusinessException extends RuntimeException {
    public ProductBusinessException() {
    }

    public ProductBusinessException(String message) {
        super(message);
    }

    public abstract ErrorType getErrorType();
    public String getErrorCode() {
        return getErrorType().getCode();
    }

    public String getErrorMessage(){
        return getMessage() == null || getMessage().isBlank() ? getErrorType().getDefaultMessage() : getMessage();
    }
}
