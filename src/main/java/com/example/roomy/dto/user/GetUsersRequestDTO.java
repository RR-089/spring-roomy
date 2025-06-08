package com.example.roomy.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUsersRequestDTO {
    private String search;

    private List<Long> ids;

    private List<String> usernames;

    private List<String> roles;
}
