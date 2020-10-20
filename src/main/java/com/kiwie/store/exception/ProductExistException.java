package com.kiwie.store.exception;

public class ProductExistException extends RuntimeException{
    public ProductExistException(String message) {
        super(message);
    }
}
