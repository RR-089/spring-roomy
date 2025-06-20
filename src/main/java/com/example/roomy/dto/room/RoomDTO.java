package com.example.roomy.dto.room;

import com.example.roomy.dto.user.UserDTO;
import com.example.roomy.dto.user.UserInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoomDTO {
    private Long id;

    private String name;

    private String status;

    private UserDTO roomMaster;

    private Set<UserInfoDTO> roomMembers;
}
