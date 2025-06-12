package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.event.UpdateEventDto;
import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.User;

public class UpdateEventMapper {
    public static UpdateEventDto toDto(Event event) {
        UpdateEventDto dto = new UpdateEventDto();
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setParticipantIds(event.getParticipants().stream().map(User::getId).toList());
        return dto;
    }
}
