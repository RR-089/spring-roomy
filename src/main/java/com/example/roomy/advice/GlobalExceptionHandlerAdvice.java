package com.example.roomy.advice;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<Object>> handleCustomException(HttpServletRequest request,
                                                                     CustomException ex) {
        log.debug("Error from {}", ex.getClass().getName());
        log.info("Error at : {}", request.getRequestURL());

        HttpStatus status = ex.getStatus();

        return ResponseEntity.status(status).body(
                ResponseDTO.builder()
                           .status(status.value())
                           .message(ex.getMessage())
                           .data(ex.getData())
                           .build()
        );
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseDTO<Object>> handlerAuthorizationDeniedException(HttpServletRequest request, AuthorizationDeniedException ex) {
        log.debug("Auth Denied Error message: {}", ex.getMessage());
        log.info("Auth Denied Error at : {}", request.getRequestURL());

        HttpStatus status = HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(
                ResponseDTO.builder()
                           .status(status.value())
                           .message("Access denied: You do not have the required authority to perform this action")
                           .data(null)
                           .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Object>> handleGeneralException(HttpServletRequest request, Exception ex) {

        log.debug("Error message: {}", ex.getMessage());
        log.info("General Error at : {}", request.getRequestURL());

        return ResponseEntity.status(500).body(
                ResponseDTO.builder()
                           .status(500)
                           .message("Unknown error occurred")
                           .data(null)
                           .build()
        );
    }
}
