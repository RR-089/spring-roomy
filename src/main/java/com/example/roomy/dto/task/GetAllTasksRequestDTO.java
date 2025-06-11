package com.example.roomy.dto.task;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetAllTasksRequestDTO {
    private String search;

    private List<Long> ids;

    private List<Long> roomIds;

    private List<Long> assigneeIds;

    private Integer minAssignee;

    private Integer maxAssignee;

    private List<String> statuses;

    private Long minDurationInMinutes;

    private Long maxDurationInMinutes;

    private LocalDateTime minStartDate;

    private LocalDateTime maxStartDate;

    private LocalDateTime minExpectedFinishedDate;

    private LocalDateTime maxExpectedFinishedDate;

    private LocalDateTime minFinishedDate;

    private LocalDateTime maxFinishedDate;
}
