package com.example.roomy.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDTO {
    private Long id;

    private String username;

    private Set<String> roles;
}
