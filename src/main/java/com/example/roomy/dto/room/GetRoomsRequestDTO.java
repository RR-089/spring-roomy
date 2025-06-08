package com.example.roomy.dto.room;

import lombok.Data;

import java.util.List;

@Data
public class GetRoomsRequestDTO {
    private String search;

    private List<Long> ids;

    private List<String> names;

    private List<String> statuses;

    private List<Long> roomMasterIds;

    private List<Long> roomMemberIds;
}
