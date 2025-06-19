package com.example.roomy.dto.userrole;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddRemoveUserRoleRequestDTO {
    @NotBlank
    private String name;
}
