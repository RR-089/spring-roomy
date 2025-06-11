package com.example.roomy.dto.userrole;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AddUserRoleRequestDTO {
    @NotBlank
    private String name;
}
