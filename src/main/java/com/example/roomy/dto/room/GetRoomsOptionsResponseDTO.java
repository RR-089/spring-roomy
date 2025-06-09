package com.example.roomy.dto.room;

import com.example.roomy.dto.common.OptionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetRoomsOptionsResponseDTO {
    private List<OptionDTO<Long>> ids;

    private List<OptionDTO<String>> names;

    private List<OptionDTO<String>> statuses;

    private List<OptionDTO<Long>> roomMasterIds;

    private List<OptionDTO<Long>> roomMemberIds;
}
