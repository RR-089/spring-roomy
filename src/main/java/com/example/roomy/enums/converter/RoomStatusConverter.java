package com.example.roomy.enums.converter;

import com.example.roomy.enums.RoomStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoomStatusConverter implements AttributeConverter<RoomStatus, String> {
    @Override
    public String convertToDatabaseColumn(RoomStatus roomStatus) {
        return roomStatus != null ? roomStatus.getValue() : null;
    }

    @Override
    public RoomStatus convertToEntityAttribute(String s) {
        return s != null ? RoomStatus.fromValue(s) : null;
    }
}
