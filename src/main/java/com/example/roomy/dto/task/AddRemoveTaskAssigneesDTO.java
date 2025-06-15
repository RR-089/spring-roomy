package com.example.roomy.dto.task;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AddRemoveTaskAssigneesDTO {
    @NotNull
    @NotEmpty
    Set<Long> assigneeIds;
}
