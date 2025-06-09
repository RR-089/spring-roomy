package com.example.roomy.dto.user;

import lombok.Data;

@Data
public class GetUsersOptionsRequestDTO {
    private boolean ids = false;

    private boolean usernames = false;

    private boolean roles = false;
}
