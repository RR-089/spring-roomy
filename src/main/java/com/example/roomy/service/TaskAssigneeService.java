package com.example.roomy.service;

import com.example.roomy.dto.task.AddRemoveTaskAssigneesDTO;
import com.example.roomy.dto.task.TaskDTO;

public interface TaskAssigneeService {
    TaskDTO bulkAddTaskAssignee(Long taskId, AddRemoveTaskAssigneesDTO dto);

    TaskDTO bulkRemoveTaskAssignee(Long taskId, AddRemoveTaskAssigneesDTO dto);
}
