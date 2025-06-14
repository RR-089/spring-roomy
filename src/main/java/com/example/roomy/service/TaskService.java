package com.example.roomy.service;


import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.task.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    PaginationDTO<List<TaskDTO>> getAllTask(GetAllTasksRequestDTO dto, Pageable pageable);

    GetTasksOptionsResponseDTO getTasksOptions(GetTasksOptionsRequestDTO dto);

    TaskDTO createTask(CreateTaskDTO dto);

    void deleteTask(Long taskId);
}
