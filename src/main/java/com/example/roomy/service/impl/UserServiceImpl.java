package com.example.roomy.service.impl;

import com.example.roomy.dto.auth.LoginRequestDTO;
import com.example.roomy.dto.auth.RegisterRequestDTO;
import com.example.roomy.dto.common.OptionDTO;
import com.example.roomy.dto.common.PaginationDTO;
import com.example.roomy.dto.user.GetUsersOptionsRequestDTO;
import com.example.roomy.dto.user.GetUsersOptionsResponseDTO;
import com.example.roomy.dto.user.GetUsersRequestDTO;
import com.example.roomy.dto.user.UserDTO;
import com.example.roomy.exception.BadRequestException;
import com.example.roomy.exception.NotFoundException;
import com.example.roomy.model.Role;
import com.example.roomy.model.User;
import com.example.roomy.repository.UserRepository;
import com.example.roomy.service.RoleService;
import com.example.roomy.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public static UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                      .id(user.getId())
                      .username(user.getUsername())
                      .roles(!user.getRoles().isEmpty()
                              ? user.getRoles()
                                    .stream()
                                    .map(Role::getName).
                                    collect(Collectors.toSet()) : null)
                      .build();
    }

    @Override
    public PaginationDTO<List<UserDTO>> findAllUsers(GetUsersRequestDTO dto,
                                                     Pageable pageable) {
        log.info("req all users");

        Page<User> userPage = userRepository.findAllUsers(
                dto.getSearch(),
                dto.getIds(),
                dto.getUsernames(),
                dto.getRoles(),
                pageable
        );

        List<UserDTO> userDTOS =
                userPage.getContent()
                        .stream()
                        .map(UserServiceImpl::mapToDTO)
                        .toList();

        return PaginationDTO.<List<UserDTO>>builder()
                            .totalPages(userPage.getTotalPages())
                            .totalRecords(userPage.getTotalElements())
                            .data(userDTOS)
                            .build();
    }

    @Override
    public GetUsersOptionsResponseDTO findUsersOptions(GetUsersOptionsRequestDTO dto) {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        List<OptionDTO<Long>> ids = new ArrayList<>();
        List<OptionDTO<String>> usernames = new ArrayList<>();
        List<OptionDTO<String>> roles = new ArrayList<>();

        for (User user : users) {
            if (dto.isIds()) {
                ids.add(OptionDTO.buildOption(user.getUsername(), user.getId()));
            }
            if (dto.isUsernames()) {
                usernames.add(OptionDTO.buildOption(user.getUsername(), user.getUsername()));
            }
            if (dto.isRoles()) {
                for (Role role : user.getRoles()) {
                    roles.add(OptionDTO.buildOption(role.getName(), role.getName()));
                }
            }
        }

        return GetUsersOptionsResponseDTO.builder()
                                         .ids(ids)
                                         .usernames(usernames)
                                         .roles(roles.stream().distinct().toList())
                                         .build();
    }

    @Override
    public UserDTO findById(Long id) {
        log.info("req user with id: {}", id);

        return mapToDTO(findEntityById(id));
    }

    @Override
    public User findEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found", null)
        );
    }

    @Override
    public UserDTO findByUsername(String username) {
        log.info("req user with username: {}", username);

        return userRepository.findByUsername(username).map(UserServiceImpl::mapToDTO)
                             .orElseThrow(() -> new NotFoundException("User not found", null));
    }

    @Override
    public UserDTO validateUserCredentials(LoginRequestDTO dto) {
        log.info("req validate user credentials: {}", dto.toString());

        User foundUser = userRepository.findByUsername(dto.getUsername())
                                       .orElseThrow(() -> new BadRequestException("Invalid credentials", null));

        if (!passwordEncoder.matches(dto.getPassword(), foundUser.getPassword())) {
            throw new BadRequestException("Invalid credentials", null);
        }

        return mapToDTO(foundUser);
    }


    @Override
    public UserDTO createUser(RegisterRequestDTO dto) {
        log.info("req create new user: {}", dto.getUsername());

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BadRequestException("Username is already taken", null);
        }

        Role defaultRole = roleService.findDefaultRole();

        User newUser = userRepository.save(User.builder()
                                               .username(dto.getUsername())
                                               .password(passwordEncoder.encode(dto.getPassword()))
                                               .roles(Set.of(
                                                       defaultRole
                                               ))
                                               .build());

        return mapToDTO(newUser);
    }


    @Override
    public void deleteUser(Long id) {
        log.info("req to delete user with id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found", null);
        }

        userRepository.deleteById(id);
    }

    @Override
    public List<User> findUsersByIds(List<Long> userIds) {
        List<Long> notFoundUserIds = new ArrayList<>();
        boolean isSomeNotFound = false;

        for (Long userId : userIds) {
            if (!userRepository.existsById(userId)) {
                isSomeNotFound = true;
                notFoundUserIds.add(userId);
            }
        }

        if (isSomeNotFound) {
            throw new BadRequestException("Some users not exists", notFoundUserIds);
        }

        return userRepository.findByIdIn(userIds);
    }
}
