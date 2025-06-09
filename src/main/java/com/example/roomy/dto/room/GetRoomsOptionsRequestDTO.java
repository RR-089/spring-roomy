package com.example.roomy.dto.room;

import lombok.Data;

@Data
public class GetRoomsOptionsRequestDTO {
    private boolean ids = false;

    private boolean names = false;

    private boolean statuses = false;

    private boolean roomMasterIds = false;

    private boolean roomMemberIds = false;
}
