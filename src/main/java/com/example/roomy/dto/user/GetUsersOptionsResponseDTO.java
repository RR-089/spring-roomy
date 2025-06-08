package com.example.roomy.dto.user;

import com.example.roomy.dto.common.OptionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetUsersOptionsResponseDTO {
    private List<OptionDTO<Long>> ids;

    private List<OptionDTO<String>> usernames;

    private List<OptionDTO<String>> roles;
}
