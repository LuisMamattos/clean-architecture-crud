package com.example.hexcrud.domain.exception;

public class DomainValidationException extends IllegalArgumentException {
    public DomainValidationException(String message) {
        super(message);
    }
}