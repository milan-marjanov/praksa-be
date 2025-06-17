package com.example.thesimpleeventapp.dto.event;

import com.example.thesimpleeventapp.model.TimeOptionType;
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
    private List<TimeOptionDto> timeOptions;
    private TimeOptionType timeOptionType;
}
