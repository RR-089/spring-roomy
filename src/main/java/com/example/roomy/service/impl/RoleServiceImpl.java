package com.example.roomy.service.impl;

import com.example.roomy.dto.role.RoleDTO;
import com.example.roomy.exception.BadRequestException;
import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Role;
import com.example.roomy.repository.RoleRepository;
import com.example.roomy.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public static RoleDTO mapToDTO(Role role) {
        return RoleDTO.builder()
                      .id(role.getId())
                      .name(role.getName())
                      .users(role.getUsers()
                                 .stream()
                                 .map(UserServiceImpl::mapToInfoDTO)
                                 .collect(Collectors.toSet())
                      )
                      .build();
    }

    @Override
    public Role findDefaultRole() {
        log.info("req find default role: USER");

        return roleRepository.findByName("USER").orElseGet(
                () -> createRole("USER")
        );
    }

    @Override
    public List<RoleDTO> findAllRoles() {
        log.info("req find all roles");

        return roleRepository.findAll()
                             .stream()
                             .map(RoleServiceImpl::mapToDTO)
                             .toList();
    }

    @Override
    public Role findById(Long id) {
        log.info("req role with id: {}", id);

        return roleRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Role not found", null)
        );
    }

    @Override
    public Role findByName(String name) {
        log.info("req role with name: {}", name);

        return roleRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("Role not found", null)
        );
    }

    @Override
    public Role createRole(String name) {
        log.info("req to create new role: {}", name);

        return roleRepository.save(Role.builder()
                                       .name(name)
                                       .build());
    }


    @Override
    public void deleteById(Long id) {
        log.info("req to delete role with id: {}", id);

        Role foundRole = roleRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Role not found", null)
        );

        validateDeleteRole(foundRole);

        roleRepository.delete(foundRole);
    }

    @Override
    public void deleteByName(String name) {
        log.info("req to delete role with name: {}", name);

        Role foundRole = roleRepository.findByName(name).orElseThrow(() ->
                new NotFoundException("Role not found", null)
        );

        validateDeleteRole(foundRole);

        roleRepository.delete(foundRole);
    }

    private void validateDeleteRole(Role role) {
        if (!role.getUsers().isEmpty()) {
            throw new BadRequestException("Cannot delete role assigned to users", null);
        }
    }
}
