package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.role.CreateRoleRequestDTO;
import com.example.roomy.model.Role;
import com.example.roomy.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Role>> createRole(@RequestBody CreateRoleRequestDTO dto) {
        Role data = roleService.createRole(dto.getName());

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(
                ResponseDTO.<Role>builder()
                           .status(status.value())
                           .message("Create role successful")
                           .data(data)
                           .build()
        );

    }
}
