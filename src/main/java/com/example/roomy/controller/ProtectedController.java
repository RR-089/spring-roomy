package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected")
@SecurityRequirement(name = "bearerAuth")
public class ProtectedController {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    //@PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> helloWorldProtected() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Authorities: " + auth.getAuthorities());
        System.out.println("Username: " + auth.getName());

        return ResponseEntity.ok(
                ResponseDTO.builder()
                           .status(200)
                           .message("Get protected route successful")
                           .data("Hello World")
                           .build()
        );
    }
}
