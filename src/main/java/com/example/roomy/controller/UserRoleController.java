package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.userrole.AddRemoveUserRoleRequestDTO;
import com.example.roomy.service.UserRoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Roles")
public class UserRoleController {
    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> addUserRole(@PathVariable("userId") Long userId,
                                                           @Valid @RequestBody AddRemoveUserRoleRequestDTO dto
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

    @DeleteMapping
    public ResponseEntity<ResponseDTO<Object>> removeUserRole(@PathVariable("userId") Long userId,
                                                              @Valid @RequestBody AddRemoveUserRoleRequestDTO dto
    ) {
        userRoleService.removeUserRole(userId, dto.getName());

        return ResponseEntity.ok(
                ResponseDTO.builder()
                           .status(HttpStatus.OK.value())
                           .message("Remove role from user successful")
                           .data(null)
                           .build()
        );

    }
}
