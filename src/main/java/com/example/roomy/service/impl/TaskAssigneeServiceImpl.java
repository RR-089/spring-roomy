package com.example.roomy.service.impl;

import com.example.roomy.dto.task.AddRemoveTaskAssigneesDTO;
import com.example.roomy.dto.task.TaskDTO;
import com.example.roomy.exception.BadRequestException;
import com.example.roomy.model.Task;
import com.example.roomy.model.User;
import com.example.roomy.repository.TaskRepository;
import com.example.roomy.service.TaskAssigneeService;
import com.example.roomy.service.TaskService;
import com.example.roomy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskAssigneeServiceImpl implements TaskAssigneeService {
    private final TaskRepository taskRepository;
    private final TaskService taskService;
    private final UserService userService;

    @Override
    public TaskDTO bulkAddTaskAssignee(Long taskId, AddRemoveTaskAssigneesDTO dto) {
        log.info("bulk add task assignee: {}", dto.getAssigneeIds());

        Task foundTask = taskService.findTaskEntity(taskId);

        Set<Long> assignedIds =
                foundTask.getAssignees().
                         stream()
                         .map(User::getId)
                         .filter((id) -> dto.getAssigneeIds().contains(id))
                         .collect(Collectors.toSet());

        if (!assignedIds.isEmpty()) {
            throw new BadRequestException("Some assignee already assigned to task", null);
        }

        if (foundTask.getAssignees().size() + dto.getAssigneeIds().size() > foundTask.getMaxAssignee()) {
            throw new BadRequestException("Assignee capacity exceeds the maximum limit"
                    , null);
        }

        List<User> foundUsers = userService.findUsersByIds(new ArrayList<>(dto.getAssigneeIds()));

        for (User assignee : foundUsers) {
            foundTask.addTaskAssignee(assignee);
        }

        return TaskServiceImpl.mapToDto(taskRepository.save(foundTask));
    }

    @Override
    public TaskDTO bulkRemoveTaskAssignee(Long taskId, AddRemoveTaskAssigneesDTO dto) {
        log.info("bulk remove task assignee: {}", dto.getAssigneeIds());

        Task foundTask = taskService.findTaskEntity(taskId);

        List<Long> notAssignedIds = new ArrayList<>();
        List<Long> assignedIds =
                foundTask.getAssignees().stream().map(User::getId).toList();

        boolean isSomeAssigneeIdsNotAssigned = false;


        for (Long assigneeId : dto.getAssigneeIds()) {
            if (!assignedIds.contains(assigneeId)) {
                isSomeAssigneeIdsNotAssigned = true;
                notAssignedIds.add(assigneeId);
            }
        }

        if (isSomeAssigneeIdsNotAssigned) {
            throw new BadRequestException("Some assignee not exists in task assignees",
                    notAssignedIds);
        }


        List<User> foundUsers = userService.findUsersByIds(new ArrayList<>(dto.getAssigneeIds()));

        for (User assignee : foundUsers) {
            foundTask.removeTaskAssignee(assignee);
        }

        return TaskServiceImpl.mapToDto(taskRepository.save(foundTask));
    }

}
