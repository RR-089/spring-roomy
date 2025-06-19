package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.role.CreateRoleRequestDTO;
import com.example.roomy.model.Role;
import com.example.roomy.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Role>> createRole(@Valid @RequestBody CreateRoleRequestDTO dto) {
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

    @DeleteMapping(value = "{roleId}")
    public ResponseEntity<ResponseDTO<Object>> deleteRole(
            @PathVariable("roleId") Long roleId
    ) {
        roleService.deleteById(roleId);
        return ResponseEntity.ok(
                ResponseDTO.builder()
                           .status(HttpStatus.OK.value())
                           .message("Delete role successful")
                           .data(null)
                           .build()
        );
    }


}
