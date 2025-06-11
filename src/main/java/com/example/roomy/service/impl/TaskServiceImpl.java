package com.example.roomy.service.impl;

import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.task.CreateTaskDTO;
import com.example.roomy.dto.task.GetAllTasksRequestDTO;
import com.example.roomy.dto.task.TaskDTO;
import com.example.roomy.enums.TaskStatus;
import com.example.roomy.exception.BadRequestException;
import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Room;
import com.example.roomy.model.Task;
import com.example.roomy.model.User;
import com.example.roomy.repository.TaskRepository;
import com.example.roomy.service.RoomService;
import com.example.roomy.service.TaskService;
import com.example.roomy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final RoomService roomService;
    private final UserService userService;

    public static TaskDTO mapToDto(Task task) {
        return TaskDTO.builder()
                      .id(task.getId())
                      .name(task.getName())
                      .description(task.getDescription())
                      .maxAssignee(task.getMaxAssignee())
                      .status(task.getStatus().toString())
                      .durationInMinutes(task.getDurationInMinutes())
                      .startDate(task.getStartDate())
                      .expectedFinishedDate(task.getExpectedFinishedDate())
                      .finishedDate(task.getFinishedDate())
                      .room(RoomServiceImpl.mapToInfoDTO(task.getRoom()))
                      .assignees(task.getAssignees().stream()
                                     .map(UserServiceImpl::mapToInfoDTO)
                                     .collect(Collectors.toSet()))
                      .build();
    }

    @Override
    public PaginationDTO<List<TaskDTO>> getAllTask(GetAllTasksRequestDTO dto, Pageable pageable) {
        log.info("req get all tasks");

        Page<Task> taskPage = taskRepository.findAllTasks(
                dto.getSearch(),
                dto.getIds(),
                dto.getRoomIds(),
                dto.getAssigneeIds(),
                dto.getStatuses(),
                dto.getMinAssignee(),
                dto.getMaxAssignee(),
                dto.getMinDurationInMinutes(),
                dto.getMaxDurationInMinutes(),
                dto.getMinStartDate(),
                dto.getMaxStartDate(),
                dto.getMinExpectedFinishedDate(),
                dto.getMaxExpectedFinishedDate(),
                dto.getMinFinishedDate(),
                dto.getMaxFinishedDate(),
                pageable
        );

        List<TaskDTO> taskDTOS = taskPage.getContent()
                                         .stream()
                                         .map(TaskServiceImpl::mapToDto)
                                         .toList();


        return PaginationDTO.<List<TaskDTO>>builder()
                            .totalRecords(taskPage.getTotalElements())
                            .totalPages(taskPage.getTotalPages())
                            .data(taskDTOS)
                            .build();
    }

    @Override
    public TaskDTO createTask(CreateTaskDTO dto) {
        log.info("req to create task in roomId: {}", dto.getRoomId());

        Room foundRoom = roomService.findEntityById(dto.getRoomId());

        Set<User> foundAssignees = dto.getAssigneeIds() != null ?
                new HashSet<>(userService.findUsersByIds(dto.getAssigneeIds()))
                : new HashSet<>();

        Task newTask = Task.builder()
                           .name(dto.getName())
                           .description(dto.getDescription())
                           .maxAssignee(dto.getMaxAssignee())
                           .status(TaskStatus.BACKLOG)
                           .room(foundRoom)
                           .assignees(new HashSet<>())
                           .build();

        for (User user : foundAssignees) {
            newTask.addTaskAssignee(user);
        }

        return mapToDto(taskRepository.save(newTask));
    }

    @Override
    public void deleteTask(Long taskId) {
        log.info("req to delete task: {}", taskId);

        Task foundTask = taskRepository.findById(taskId).orElseThrow(
                () -> new NotFoundException("Task not found", null)
        );

        if (foundTask.getFinishedDate() != null && foundTask.getStatus() == TaskStatus.FINISHED) {
            throw new BadRequestException("Finished tasks cannot have new assignees",
                    null);
        }

        taskRepository.delete(foundTask);
    }
}
