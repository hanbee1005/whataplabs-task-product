package com.whataplabs.task.product.whataplabstaskproduct.infrastructure.handler;

import com.whataplabs.task.product.whataplabstaskproduct.domain.exception.ErrorType;

public class CustomLockException extends RuntimeException {

    public CustomLockException() {
    }

    public CustomLockException(String message) {
        super(message);
    }

    public ErrorType getErrorType() {
        return ErrorType.LOCK_ACQUISITION_FAILED;
    };

    public String getErrorCode() {
        return getErrorType().getCode();
    }

    public String getErrorMessage(){
        return getMessage() == null || getMessage().isBlank() ? getErrorType().getDefaultMessage() : getMessage();
    }
}
