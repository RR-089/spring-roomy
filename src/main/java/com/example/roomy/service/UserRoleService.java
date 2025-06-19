package com.example.roomy.service;

public interface UserRoleService {
    void addUserRole(Long userId, String roleName);

    void removeUserRole(Long userId, String roleName);
}
