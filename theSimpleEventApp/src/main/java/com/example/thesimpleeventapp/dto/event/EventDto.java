package com.example.thesimpleeventapp.dto.event;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String title;
    private String description;
    private ParticipantDto creator;
    private List<ParticipantDto> participants;
}
