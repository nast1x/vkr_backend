package com.ssau.kurs_back.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(resourceName + " не найден: " + id);
    }
}
