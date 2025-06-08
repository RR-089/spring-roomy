package com.example.roomy.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus status;
    private final Object data;

    public CustomException(HttpStatus status, String message, Object data) {
        super(message);
        this.status = status;
        this.data = data;
    }
}
