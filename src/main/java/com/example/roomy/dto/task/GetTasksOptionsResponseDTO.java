package com.example.roomy.dto.task;

import com.example.roomy.dto.common.OptionDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetTasksOptionsResponseDTO {
    private List<OptionDTO<Long>> ids;
    private List<OptionDTO<Long>> roomIds;
    private List<OptionDTO<Long>> assigneeIds;
    private List<OptionDTO<String>> statuses;
}
