package com.example.roomy.exception;


import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {
    public NotFoundException(String message, Object data) {
        super(HttpStatus.NOT_FOUND, message, data);
    }
}
