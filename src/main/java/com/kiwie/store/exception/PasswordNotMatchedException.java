package com.kiwie.store.exception;

public class PasswordNotMatchedException extends RuntimeException {

    public PasswordNotMatchedException(String message) {
        super(message);
    }
}
