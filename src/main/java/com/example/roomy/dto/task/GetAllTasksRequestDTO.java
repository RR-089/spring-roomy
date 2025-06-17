package com.example.roomy.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetAllTasksRequestDTO {
    @Schema(description = "Search string", example = "GET /users endpoint")
    private String search;

    @Schema(description = "Task ids array, example: [1, 2, 3]")
    private List<Long> ids;

    @Schema(description = "Room ids array, example: [1, 2, 3]")
    private List<Long> roomIds;

    @Schema(description = "Assignee ids array, example: [1, 2, 3]")
    private List<Long> assigneeIds;

    @Schema(description = "Min assignee limit", examples = "1")
    private Integer minAssignee;

    @Schema(description = "Max assignee limit", examples = "10")
    private Integer maxAssignee;

    @Schema(description = "Task statuses array", examples = "['Backlog', 'Sprint']")
    private List<String> statuses;

    @Schema(description = "Min task duration in minutes", examples = "60")
    private Long minDurationInMinutes;

    @Schema(description = "Max task duration in minutes", examples = "360")
    private Long maxDurationInMinutes;

    @Schema(description = "Min task start date", examples = "2025-06-17T14:30:00")
    private LocalDateTime minStartDate;

    @Schema(description = "Max task start date", examples = "2025-07-17T14:30:00")
    private LocalDateTime maxStartDate;

    @Schema(
            description = "Min expected task finished date",
            examples = "2025-06-17T14:30:00")
    private LocalDateTime minExpectedFinishedDate;

    @Schema(
            description = "Max expected task finished date",
            examples = "2025-07-17T14:30:00")
    private LocalDateTime maxExpectedFinishedDate;

    @Schema(
            description = "Min actual task finished date",
            examples = "2025-06-17T14:30:00")
    private LocalDateTime minFinishedDate;

    @Schema(
            description = "Max actual task finished date",
            examples = "2025-07-17T14:30:00")
    private LocalDateTime maxFinishedDate;
}
