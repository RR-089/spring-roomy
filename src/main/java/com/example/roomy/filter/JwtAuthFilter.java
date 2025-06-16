package com.example.roomy.filter;

import com.example.roomy.dto.common.ResponseDTO;
import com.example.roomy.exception.BadRequestException;
import com.example.roomy.exception.CustomException;
import com.example.roomy.exception.UnauthorizedException;
import com.example.roomy.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return path.contains("/auth") || path.contains("swagger")
                || path.contains("api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        validateHeader(authHeader, response);

        String token = authHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);
            List<String> roles = jwtUtil.extractRoles(token);
            List<GrantedAuthority> authorities = roles.stream()
                                                      .map((role) -> new SimpleGrantedAuthority("ROLE_" + role))
                                                      .collect(Collectors.toList());
            UserDetails userDetails = User.builder()
                                          .username(username)
                                          .password("")
                                          .authorities(authorities)
                                          .build();

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());


            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception ex) {
            handleCustomException(
                    new UnauthorizedException("Invalid token", null),
                    response
            );
        }

        filterChain.doFilter(request, response);
    }

    private void validateHeader(String authHeader, HttpServletResponse response) throws IOException {
        try {
            if (authHeader == null) {
                throw new UnauthorizedException("Access denied: No token provided",
                        null);
            }

            if (!authHeader.startsWith("Bearer")) {
                throw new BadRequestException("Only accepted Bearer token auth", null);
            }
        } catch (CustomException ex) {
            handleCustomException(ex, response);
        }
    }

    private void handleCustomException(CustomException ex,
                                       HttpServletResponse response) throws JsonProcessingException, IOException {
        response.setStatus(ex.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String json = new ObjectMapper().writeValueAsString(
                ResponseDTO.builder()
                           .status(HttpStatus.UNAUTHORIZED.value())
                           .message(ex.getMessage())
                           .data(ex.getData())
                           .build()
        );

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
