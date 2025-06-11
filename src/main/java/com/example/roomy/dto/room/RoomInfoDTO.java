package com.example.roomy.dto.room;

import com.example.roomy.dto.user.UserDTO;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RoomInfoDTO {
    private Long id;

    private String name;

    private String status;

    private UserDTO roomMaster;
}
