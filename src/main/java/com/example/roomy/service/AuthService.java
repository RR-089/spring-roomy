package com.example.roomy.service;


import com.example.roomy.dto.auth.LoginRequestDTO;
import com.example.roomy.dto.auth.RegisterRequestDTO;
import com.example.roomy.dto.auth.TokenResponseDTO;

public interface AuthService {
    TokenResponseDTO login(LoginRequestDTO dto);

    TokenResponseDTO register(RegisterRequestDTO dto);
}
