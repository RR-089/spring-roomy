package com.example.roomy.service;

import com.example.roomy.dto.auth.LoginRequestDTO;
import com.example.roomy.dto.auth.RegisterRequestDTO;
import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.user.GetUsersOptionsRequestDTO;
import com.example.roomy.dto.user.GetUsersOptionsResponseDTO;
import com.example.roomy.dto.user.GetUsersRequestDTO;
import com.example.roomy.dto.user.UserDTO;
import com.example.roomy.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDTO findById(Long id);

    User findEntityById(Long id);

    PaginationDTO<List<UserDTO>> findAllUsers(GetUsersRequestDTO dto, Pageable pageable);

    GetUsersOptionsResponseDTO findUsersOptions(GetUsersOptionsRequestDTO dto);

    UserDTO findByUsername(String username);

    UserDTO createUser(RegisterRequestDTO dto);

    void deleteUser(Long id);

    UserDTO validateUserCredentials(LoginRequestDTO dto);

    List<User> findUsersByIds(List<Long> userIds);
}
