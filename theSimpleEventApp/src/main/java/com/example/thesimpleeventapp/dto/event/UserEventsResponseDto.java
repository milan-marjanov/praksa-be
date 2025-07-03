package com.example.thesimpleeventapp.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventsResponseDto {
    private List<EventDto> createdEvents;
    private List<EventDto> participantEvents;;
}
