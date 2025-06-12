package com.example.thesimpleeventapp.dto.event;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventDto {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
    private List<Long> participantIds;
}
