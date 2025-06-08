package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.userrole.AddUserRoleRequestDTO;
import com.example.roomy.service.UserRoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class UserRoleController {
    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> addUserRole(@PathVariable("userId") Long userId,
                                                           @RequestBody AddUserRoleRequestDTO dto
    ) {
        userRoleService.addUserRole(userId, dto.getName());

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(
                ResponseDTO.builder()
                           .status(status.value())
                           .message("Add role to user successful")
                           .data(null)
                           .build()
        );

    }
}
