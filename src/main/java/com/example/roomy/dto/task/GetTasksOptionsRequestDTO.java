package com.example.roomy.dto.task;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetTasksOptionsRequestDTO {
    private boolean ids = false;
    private boolean roomIds = false;
    private boolean assigneeIds = false;
    private boolean statuses = false;
}
