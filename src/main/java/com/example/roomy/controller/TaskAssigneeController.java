package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.task.AddRemoveTaskAssigneesDTO;
import com.example.roomy.dto.task.TaskDTO;
import com.example.roomy.service.TaskAssigneeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks/{taskId}/assignees")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Task Assignees")
@PreAuthorize("hasRole('ADMIN') or hasRole('ROOM_MASTER')")
public class TaskAssigneeController {
    private final TaskAssigneeService taskAssigneeService;

    @PostMapping
    public ResponseEntity<ResponseDTO<TaskDTO>> bulkAddTaskAssignees(
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody AddRemoveTaskAssigneesDTO dto
    ) {
        TaskDTO data = taskAssigneeService.bulkAddTaskAssignee(taskId, dto);
        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(
                ResponseDTO.<TaskDTO>builder()
                           .status(status.value())
                           .message("Bulk add task assignees successful")
                           .data(data)
                           .build()
        );

    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO<TaskDTO>> bulkRemoveTaskAssignees(
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody AddRemoveTaskAssigneesDTO dto
    ) {
        TaskDTO data = taskAssigneeService.bulkRemoveTaskAssignee(taskId, dto);

        return ResponseEntity.ok(
                ResponseDTO.<TaskDTO>builder()
                           .status(HttpStatus.OK.value())
                           .message("Bulk remove assignees successful")
                           .data(data)
                           .build()
        );
    }

}
