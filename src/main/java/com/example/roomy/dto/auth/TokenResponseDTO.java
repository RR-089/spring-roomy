package com.example.roomy.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponseDTO {
    private String token;
}
