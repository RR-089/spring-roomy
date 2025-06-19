package com.example.roomy.controller;

import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.dto.user.GetUsersOptionsRequestDTO;
import com.example.roomy.dto.user.GetUsersOptionsResponseDTO;
import com.example.roomy.dto.user.GetUsersRequestDTO;
import com.example.roomy.dto.user.UserDTO;
import com.example.roomy.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseDTO<PaginationDTO<List<UserDTO>>>> getAllUsers(
            @ParameterObject GetUsersRequestDTO dto,
            @ParameterObject Pageable pageable) {

        PaginationDTO<List<UserDTO>> data = userService.findAllUsers(dto, pageable);

        return ResponseEntity.ok(
                ResponseDTO.<PaginationDTO<List<UserDTO>>>builder()
                           .status(HttpStatus.OK.value())
                           .message("Get users successful")
                           .data(data)
                           .build()
        );
    }

    @GetMapping(value = "/options")
    public ResponseEntity<ResponseDTO<GetUsersOptionsResponseDTO>> getAllUsersOptions(
            @ParameterObject GetUsersOptionsRequestDTO dto) {
        GetUsersOptionsResponseDTO data = userService.findUsersOptions(dto);

        return ResponseEntity.ok(
                ResponseDTO.<GetUsersOptionsResponseDTO>builder()
                           .status(HttpStatus.OK.value())
                           .message("Get users options successful")
                           .data(data)
                           .build()
        );

    }
}
