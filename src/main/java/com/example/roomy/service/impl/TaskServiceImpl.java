package com.example.roomy.service.impl;

import com.example.roomy.dto.common.OptionDTO;
import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.task.*;
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
import com.example.roomy.util.DateTimeUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final EntityManager entityManager;
    private final TaskRepository taskRepository;
    private final RoomService roomService;
    private final UserService userService;
    private final DateTimeUtil dateTimeUtil;

    public static TaskDTO mapToDto(Task task) {
        return TaskDTO.builder()
                      .id(task.getId())
                      .name(task.getName())
                      .description(task.getDescription())
                      .maxAssignee(task.getMaxAssignee())
                      .status(task.getStatus().getValue())
                      .durationInMinutes(task.getDurationInMinutes())
                      .startDate(task.getStartDate())
                      .expectedStartDate(task.getExpectedStartDate())
                      .expectedFinishedDate(task.getExpectedFinishedDate())
                      .finishedDate(task.getFinishedDate())
                      .room(RoomServiceImpl.mapToInfoDTO(task.getRoom()))
                      .assignees(task.getAssignees().stream()
                                     .map(UserServiceImpl::mapToInfoDTO)
                                     .collect(Collectors.toSet()))
                      .build();
    }

    @Override
    public Task findTaskEntity(Long id) {
        log.info("req task: {}", id);
        return taskRepository.findById(id).orElseThrow(() -> new NotFoundException(
                "Task not found", null));
    }

    @Override
    public PaginationDTO<List<TaskDTO>> findAllTask(GetAllTasksRequestDTO dto, Pageable pageable) {
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
    public GetTasksOptionsResponseDTO findTasksOptions(GetTasksOptionsRequestDTO dto) {
        StringBuilder sb = new StringBuilder("select ");
        Map<String, String> fieldToAlias = getFieldToAlias(dto);

        List<String> fields = new ArrayList<>(fieldToAlias.keySet());
        sb.append(String.join(", ", fields));
        sb.append(" from Task t");

        if (dto.isRoomIds()) {
            sb.append(" join t.room r");
        }
        if (dto.isAssigneeIds()) {
            sb.append(" left join t.assignees a");
        }

        Query query = entityManager.createQuery(sb.toString());
        List<?> results = query.getResultList();

        Set<OptionDTO<Long>> ids = new LinkedHashSet<>();
        Set<OptionDTO<Long>> roomIds = new LinkedHashSet<>();
        Set<OptionDTO<Long>> assigneeIds = new LinkedHashSet<>();
        Set<OptionDTO<String>> statuses = new LinkedHashSet<>();

        for (Object result : results) {
            Object[] row = fieldToAlias.size() == 1 ? new Object[]{result} : (Object[]) result;

            Long taskId = null, roomId = null, assigneeId = null;
            String taskName = null, roomName = null, assigneeName = null;

            int index = 0;
            for (String alias : fieldToAlias.values()) {
                Object value = row[index++];
                switch (alias) {
                    case "id" -> taskId = (Long) value;
                    case "taskName" -> taskName = (String) value;
                    case "roomId" -> roomId = (Long) value;
                    case "roomName" -> roomName = (String) value;
                    case "assigneeId" -> assigneeId = (Long) value;
                    case "assigneeName" -> assigneeName = (String) value;
                    case "status" ->
                            statuses.add(OptionDTO.buildOption(String.valueOf(value), String.valueOf(value)));
                }
            }

            if (taskId != null && taskName != null) {
                ids.add(OptionDTO.buildOption(taskName, taskId));
            }

            if (roomId != null && roomName != null) {
                roomIds.add(OptionDTO.buildOption(roomName, roomId));
            }

            if (assigneeId != null && assigneeName != null) {
                assigneeIds.add(OptionDTO.buildOption(assigneeName, assigneeId));
            }
        }

        return GetTasksOptionsResponseDTO.builder()
                                         .ids(dto.isIds() ? new ArrayList<>(ids) : null)
                                         .roomIds(dto.isRoomIds() ? new ArrayList<>(roomIds) : null)
                                         .assigneeIds(dto.isAssigneeIds() ? new ArrayList<>(assigneeIds) : null)
                                         .statuses(dto.isStatuses() ? new ArrayList<>(statuses) : null)
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
    public TaskDTO updateTask(Long taskId, UpdateTaskDTO dto) {
        Task foundTask = this.findTaskEntity(taskId);

        foundTask.setName(dto.getName());
        foundTask.setDescription(dto.getDescription());

        if (!Objects.equals(foundTask.getMaxAssignee(), dto.getMaxAssignee()) && foundTask.getStatus() == TaskStatus.FINISHED) {
            throw new BadRequestException("Cannot update max assignees for a finished task", null);
        }

        if (!foundTask.getAssignees().isEmpty()) {
            int currentAssigneesSize = foundTask.getAssignees().size();

            if (dto.getMaxAssignee() < currentAssigneesSize) {
                throw new BadRequestException("New limit is less than current assignees", null);
            }
        }

        foundTask.setMaxAssignee(dto.getMaxAssignee());

        return mapToDto(taskRepository.save(foundTask));
    }

    @Override
    public TaskDTO updateTaskDuration(Long taskId, Long duration) {
        Task foundTask = findTaskEntity(taskId);

        foundTask.setDurationInMinutes(duration);

        return mapToDto(taskRepository.save(foundTask));
    }

    @Transactional
    @Override
    public TaskDTO updateTaskStatus(Long taskId, String status) {

        TaskStatus nexStatus = TaskStatus.fromValue(status);

        Task foundTask = findTaskEntity(taskId);

        log.info("Update task status from: {}, to: {}", foundTask.getStatus(),
                nexStatus.getValue());

        validateTaskUpdateNextStatus(foundTask, nexStatus);

        foundTask.setStatus(nexStatus);

        if (nexStatus.equals(TaskStatus.SPRINT)) {
            LocalDateTime startDate =
                    dateTimeUtil.calculateExpectedDate(LocalDateTime.now(), 0);

            LocalDateTime expectedStartDate =
                    findMaxExpectedStartDate(foundTask, startDate);

            if (expectedStartDate == null) {
                expectedStartDate = startDate;
            }

            LocalDateTime expectedFinishedDate =
                    dateTimeUtil.calculateExpectedDate(expectedStartDate, foundTask.getDurationInMinutes());

            foundTask.setStartDate(startDate);
            foundTask.setExpectedStartDate(expectedStartDate);
            foundTask.setExpectedFinishedDate(expectedFinishedDate);
        } else {
            LocalDateTime finishedDate =
                    dateTimeUtil.calculateExpectedDate(LocalDateTime.now(), 0);

            foundTask.setFinishedDate(finishedDate);
        }

        return mapToDto(taskRepository.save(foundTask));
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

    private Map<String, String> getFieldToAlias(GetTasksOptionsRequestDTO dto) {
        Map<String, String> fieldToAlias = new LinkedHashMap<>();

        // Select fields based on flags
        if (dto.isIds()) {
            fieldToAlias.put("t.id", "id");
            fieldToAlias.put("t.name", "taskName"); // For label
        }
        if (dto.isRoomIds()) {
            fieldToAlias.put("r.id", "roomId");
            fieldToAlias.put("r.name", "roomName"); // For label
        }
        if (dto.isAssigneeIds()) {
            fieldToAlias.put("a.id", "assigneeId");
            fieldToAlias.put("a.username", "assigneeName"); // For label
        }
        if (dto.isStatuses()) {
            fieldToAlias.put("t.status", "status");
        }

        if (fieldToAlias.isEmpty()) {
            throw new IllegalArgumentException("At least one field must be selected.");
        }
        return fieldToAlias;
    }

    private void validateTaskUpdateNextStatus(Task task, TaskStatus status) {
        if (task.getDurationInMinutes() == null) {
            throw new BadRequestException("Cannot update task status: duration not set.", null);
        }

        if (task.getAssignees().isEmpty()) {
            throw new BadRequestException("Cannot update task status: empty assignees",
                    null);
        }

        Map<TaskStatus, TaskStatus> allowedTransitionStatuses = Map.of(
                TaskStatus.BACKLOG, TaskStatus.SPRINT,
                TaskStatus.SPRINT, TaskStatus.FINISHED
        );

        TaskStatus validNextStatus = allowedTransitionStatuses.get(task.getStatus());

        if (!status.equals(validNextStatus)) {
            throw new BadRequestException("Invalid task status transition", null);
        }
    }

    private LocalDateTime findMaxExpectedStartDate(Task task, LocalDateTime startDate) {
        LocalDateTime expectedStartDate = startDate;

        for (User user : task.getAssignees()) {
            LocalDateTime userLatestFinish =
                    taskRepository.findMaxExpectedFinishedDateTask(user.getId());

            if (userLatestFinish != null && expectedStartDate.isBefore(userLatestFinish)) {
                expectedStartDate = userLatestFinish;
            }
        }

        return expectedStartDate;
    }
}
