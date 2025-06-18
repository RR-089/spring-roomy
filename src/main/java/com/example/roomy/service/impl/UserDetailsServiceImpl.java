package com.example.roomy.service.impl;

import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Role;
import com.example.roomy.model.User;
import com.example.roomy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found", null)
        );

        Set<String> roles =
                foundUser.getRoles()
                         .stream()
                         .map(Role::getName)
                         .collect(Collectors.toSet());

        Set<GrantedAuthority> grantedAuthorities = roles.stream()
                                                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                                        .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User.builder()
                                                                 .username(foundUser.getUsername())
                                                                 .password(foundUser.getUsername())
                                                                 .authorities(grantedAuthorities)
                                                                 .build();

    }
}
