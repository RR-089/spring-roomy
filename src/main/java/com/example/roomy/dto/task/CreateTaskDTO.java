package com.example.roomy.dto.task;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateTaskDTO {
    @NotNull
    private Long roomId;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @Min(1)
    private Integer maxAssignee;

    private List<Long> assigneeIds;
}
