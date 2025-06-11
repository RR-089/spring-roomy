package com.example.roomy.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateRoleRequestDTO {
    @NotBlank
    private String name;
}
