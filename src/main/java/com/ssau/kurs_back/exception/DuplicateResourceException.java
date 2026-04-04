package com.ssau.kurs_back.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String resourceName, Object value) {
        super(resourceName + " уже существует: " + value);
    }
}
