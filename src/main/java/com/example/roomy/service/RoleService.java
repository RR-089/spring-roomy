package com.example.roomy.service;


import com.example.roomy.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAllRoles();

    Role findById(Long id);

    Role findDefaultRole();

    Role findByName(String name);

    Role createRole(String name);

    void deleteById(Long id);

    void deleteByName(String name);
}
