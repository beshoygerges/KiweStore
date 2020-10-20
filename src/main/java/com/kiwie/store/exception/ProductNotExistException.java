package com.kiwie.store.exception;

public class ProductNotExistException extends RuntimeException {

    public ProductNotExistException(String message) {
        super(message);
    }
}
