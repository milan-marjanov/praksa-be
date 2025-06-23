package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.event.TimeOptionDto;
import com.example.thesimpleeventapp.model.TimeOption;

public class TimeOptionMapper {
    public static TimeOptionDto toDto(TimeOption timeOption) {
        TimeOptionDto dto = new TimeOptionDto();
        dto.setId(timeOption.getId());
        dto.setMaxCapacity(timeOption.getMaxCapacity());
        dto.setStartTime(timeOption.getStartTime());
        dto.setEndTime(timeOption.getEndTime());
        dto.setCreatedAt(timeOption.getCreatedAt());
        return dto;
    }

    public static TimeOption toEntity(TimeOptionDto dto) {
        TimeOption timeOption = new TimeOption();
        timeOption.setMaxCapacity(dto.getMaxCapacity());
        timeOption.setStartTime(dto.getStartTime());
        timeOption.setEndTime(dto.getEndTime());
        timeOption.setCreatedAt(dto.getCreatedAt());
        return timeOption;
    }
}
