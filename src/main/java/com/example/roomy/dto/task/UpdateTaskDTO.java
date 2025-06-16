package com.example.roomy.dto.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskDTO {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(0)
    private Integer maxAssignee;
}
