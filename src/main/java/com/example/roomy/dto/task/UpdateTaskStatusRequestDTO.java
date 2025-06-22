package com.example.roomy.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskStatusRequestDTO {
    @NotNull
    private String nextStatus;
}
