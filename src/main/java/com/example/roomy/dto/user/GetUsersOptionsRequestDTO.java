package com.example.roomy.dto.user;

import lombok.Data;

@Data
public class GetUsersOptionsRequestDTO {
    private Boolean ids = false;

    private Boolean usernames = false;

    private Boolean roles = false;
}
