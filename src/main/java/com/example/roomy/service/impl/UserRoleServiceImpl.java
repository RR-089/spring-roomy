package com.example.roomy.service.impl;

import com.example.roomy.exception.BadRequestException;
import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Role;
import com.example.roomy.model.User;
import com.example.roomy.repository.RoleRepository;
import com.example.roomy.repository.UserRepository;
import com.example.roomy.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void addUserRole(Long userId, String roleName) {
        log.info("Add new role: {}, to userId: {}", roleName, userId);

        Role foundRole = roleRepository.findByName(roleName).orElseThrow(
                () -> new NotFoundException("Role not found", null)
        );

        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", null)
        );

        foundUser.addRole(foundRole);


        userRepository.save(foundUser);
    }

    @Override
    public void removeUserRole(Long userId, String roleName) {
        log.info("Remove role: {}, from userId: {}", roleName, userId);

        Role foundRole = roleRepository.findByName(roleName).orElseThrow(
                () -> new NotFoundException("Role not found", null)
        );

        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User not found", null)
        );

        boolean isUserHasFoundRole = foundUser.getRoles().stream()
                                              .anyMatch(userRole -> userRole.equals(foundRole));

        if (!isUserHasFoundRole) {
            throw new BadRequestException(
                    String.format("User does not have the role: %s", foundRole.getName()),
                    null
            );
        }

        foundUser.removeRole(foundRole);

        userRepository.save(foundUser);
    }

}
