package com.example.roomy.exception;


import org.springframework.http.HttpStatus;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(String message, Object data) {
        super(HttpStatus.UNAUTHORIZED, message, data);
    }
}
