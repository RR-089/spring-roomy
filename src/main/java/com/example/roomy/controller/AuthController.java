package com.example.roomy.controller;

import com.example.roomy.dto.auth.LoginRequestDTO;
import com.example.roomy.dto.auth.RegisterRequestDTO;
import com.example.roomy.dto.auth.TokenResponseDTO;
import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        TokenResponseDTO token = authService.login(loginRequestDTO);

        return ResponseEntity.ok(
                ResponseDTO.<TokenResponseDTO>builder()
                           .status(HttpStatus.OK.value())
                           .message("Login successful")
                           .data(token)
                           .build()
        );
    }

    @PostMapping(value = "register")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO
    ) {
        TokenResponseDTO token = authService.register(registerRequestDTO);

        HttpStatus status = HttpStatus.CREATED;

        return ResponseEntity.status(status).body(
                ResponseDTO.<TokenResponseDTO>builder()
                           .status(status.value())
                           .message("Register successful")
                           .data(token)
                           .build()
        );

    }
}
