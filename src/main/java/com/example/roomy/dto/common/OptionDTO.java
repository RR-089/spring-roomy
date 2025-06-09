package com.example.roomy.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionDTO<T> {
    private String label;
    private T value;

    public static <T> OptionDTO<T> buildOption(String label, T value) {
        return OptionDTO.<T>builder()
                        .label(label)
                        .value(value)
                        .build();
    }
}
