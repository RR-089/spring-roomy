package com.example.roomy.service;


import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.task.*;
import com.example.roomy.model.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    PaginationDTO<List<TaskDTO>> findAllTask(GetAllTasksRequestDTO dto, Pageable pageable);

    GetTasksOptionsResponseDTO findTasksOptions(GetTasksOptionsRequestDTO dto);

    Task findTaskEntity(Long id);

    TaskDTO createTask(CreateTaskDTO dto);

    void deleteTask(Long taskId);
}
