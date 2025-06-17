package com.example.roomy.controller;

import com.example.roomy.dto.common.ResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Protected", description = "Just example of protected route")
public class ProtectedController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    //@PreAuthorize("hasAuthority('ROLE_USER')")
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
