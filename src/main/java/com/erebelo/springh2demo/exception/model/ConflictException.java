package com.erebelo.springh2demo.exception.model;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
