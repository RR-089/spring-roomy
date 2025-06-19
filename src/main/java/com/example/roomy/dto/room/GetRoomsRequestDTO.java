package com.example.roomy.dto.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class GetRoomsRequestDTO {
    @Schema(description = "Search string", examples = "BE Programmer Room")
    private String search;

    @Schema(description = "Task ids array, example: [1, 2, 3]")
    private List<Long> ids;

    @Schema(description = "Room names array, example: ['Room 1', 'Room 2']")
    private List<String> names;

    @Schema(description = "Room statuses array, example: ['BACKLOG', 'SPRINT', " +
            "'FINISHED']")
    private List<String> statuses;

    @Schema(description = "Room master ids array, example: [1, 2, 3]")
    private List<Long> roomMasterIds;

    @Schema(description = "Room member ids array, example: [1, 2, 3]")
    private List<Long> roomMemberIds;
}
