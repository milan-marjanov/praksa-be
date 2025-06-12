package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.event.EventDto;
import com.example.thesimpleeventapp.dto.event.ParticipantDto;
import com.example.thesimpleeventapp.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static EventDto toDto(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setCreator(UserMapper.toDto(event.getCreator()));
        List<ParticipantDto> participantDto = event.getParticipants().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

        dto.setParticipants(participantDto);
        return dto;
    }
}
