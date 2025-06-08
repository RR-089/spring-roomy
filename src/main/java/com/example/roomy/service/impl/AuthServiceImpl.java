package com.example.roomy.service.impl;

import com.example.roomy.dto.auth.LoginRequestDTO;
import com.example.roomy.dto.auth.RegisterRequestDTO;
import com.example.roomy.dto.auth.TokenResponseDTO;
import com.example.roomy.dto.user.UserDTO;
import com.example.roomy.service.AuthService;
import com.example.roomy.service.UserService;
import com.example.roomy.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;


    @Transactional
    @Override
    public TokenResponseDTO login(LoginRequestDTO dto) {
        log.info("req to login user: {}", dto.getUsername());

        UserDTO validatedUser = userService.validateUserCredentials(dto);

        String token = jwtUtil.generateToken(validatedUser.getUsername(),
                validatedUser.getRoles());

        return TokenResponseDTO.builder()
                               .token(token)
                               .build();
    }

    @Transactional
    @Override
    public TokenResponseDTO register(RegisterRequestDTO dto) {
        log.info("req to register new user: {}", dto.getUsername());

        UserDTO createdUser = userService.createUser(dto);

        String token = jwtUtil.generateToken(createdUser.getUsername(), createdUser.getRoles());

        return TokenResponseDTO.builder()
                               .token(token)
                               .build();
    }
}
