package com.example.roomy.enums.converter;

import com.example.roomy.enums.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {
    @Override
    public String convertToDatabaseColumn(TaskStatus taskStatus) {
        return taskStatus != null ? taskStatus.getValue() : null;
    }

    @Override
    public TaskStatus convertToEntityAttribute(String s) {
        return s != null ? TaskStatus.fromValue(s) : null;
    }
}
