package com.example.roomy.dto.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTaskDurationDTO {
    @NotNull
    @Min(0)
    private Long duration;
}
