package com.example.roomy.service.impl;

import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Role;
import com.example.roomy.repository.RoleRepository;
import com.example.roomy.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findDefaultRole() {
        log.info("req find default role: USER");

        return roleRepository.findByName("USER").orElseGet(
                () -> createRole("USER")
        );
    }

    @Override
    public List<Role> findAllRoles() {
        log.info("req find all roles");

        return roleRepository.findAll();
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

        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("Role not found", null);
        }

        roleRepository.deleteById(id);
    }

    @Override
    public void deleteByName(String name) {
        log.info("req to delete role with name: {}", name);

        if (!roleRepository.existsByName(name)) {
            throw new NotFoundException("Role not found", null);
        }

        roleRepository.deleteByName(name);
    }
}
