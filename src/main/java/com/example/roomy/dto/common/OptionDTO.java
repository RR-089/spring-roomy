package com.example.roomy.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionDTO<T> {
    private T value;
    private String label;
}
