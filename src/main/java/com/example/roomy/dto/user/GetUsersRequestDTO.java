package com.example.roomy.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUsersRequestDTO {
    @Schema(description = "Search string", examples = "John Doe")
    private String search;

    @Schema(description = "User ids array, example: [1, 2, 3]")
    private List<Long> ids;

    @Schema(description = "Usernames array, example: ['John Doe', 'Jane Doe']")
    private List<String> usernames;

    @Schema(description = "User roles array, example: ['USER', 'ADMIN']")
    private List<String> roles;
}
