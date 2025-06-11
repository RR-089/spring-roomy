package com.example.roomy.controller;

import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.task.CreateTaskDTO;
import com.example.roomy.dto.task.GetAllTasksRequestDTO;
import com.example.roomy.dto.task.TaskDTO;
import com.example.roomy.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TaskController {
    private final TaskService taskService;


    @GetMapping
    public ResponseEntity<ResponseDTO<PaginationDTO<List<TaskDTO>>>> getAllTasks(GetAllTasksRequestDTO dto, Pageable pageable) {
        PaginationDTO<List<TaskDTO>> data = taskService.getAllTask(dto, pageable);

        return ResponseEntity.ok(
                ResponseDTO.<PaginationDTO<List<TaskDTO>>>builder()
                           .status(HttpStatus.OK.value())
                           .message("Get all tasks successful")
                           .data(data)
                           .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<TaskDTO>> createTask(@Valid @RequestBody CreateTaskDTO dto) {
        HttpStatus status = HttpStatus.CREATED;
        TaskDTO data = taskService.createTask(dto);

        return ResponseEntity.status(status).body(
                ResponseDTO.<TaskDTO>builder()
                           .status(status.value())
                           .message("Create task successful")
                           .data(data)
                           .build()
        );
    }

    @DeleteMapping(value = "{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
    public ResponseEntity<ResponseDTO<Object>> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);

        return ResponseEntity.ok(
                ResponseDTO.builder()
                           .status(HttpStatus.OK.value())
                           .message("Delete task successful")
                           .data(null)
                           .build()
        );
    }

}
