package com.example.roomy.dto.role;

import com.example.roomy.dto.user.UserInfoDTO;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class RoleDTO {
    private Long id;

    private String name;

    private Set<UserInfoDTO> users;
}
