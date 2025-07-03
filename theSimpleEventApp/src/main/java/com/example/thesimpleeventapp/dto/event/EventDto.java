package com.example.thesimpleeventapp.dto.event;


import com.example.thesimpleeventapp.model.RestaurantOptionType;
import com.example.thesimpleeventapp.model.TimeOptionType;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime votingDeadline;
    private List<TimeOptionDto> timeOptions;
    private TimeOptionType timeOptionType;
    private RestaurantOptionType restaurantOptionType;
    private List<RestaurantOptionDto> restaurantOptions;
}