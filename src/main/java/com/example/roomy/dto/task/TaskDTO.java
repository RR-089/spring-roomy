package com.example.roomy.dto.task;

import com.example.roomy.dto.room.RoomInfoDTO;
import com.example.roomy.dto.user.UserInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class TaskDTO {
    private Long id;

    private String name;

    private String description;

    private Integer maxAssignee;

    private String status;

    private Long durationInMinutes;

    private LocalDateTime startDate;

    private LocalDateTime expectedFinishedDate;

    private LocalDateTime finishedDate;

    private RoomInfoDTO room;

    private Set<UserInfoDTO> assignees;
}
