package com.example.roomy.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO<T> {
    private long status;
    private String message;
    private T data;
}
