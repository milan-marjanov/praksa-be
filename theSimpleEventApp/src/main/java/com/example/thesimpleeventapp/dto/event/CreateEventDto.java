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
public class CreateEventDto {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
    private List<Long> participantIds;
    private LocalDateTime votingDeadline;
    private TimeOptionType timeOptionType;
    private List<TimeOptionDto> timeOptions;
    private RestaurantOptionType restaurantOptionType;
    private List<RestaurantOptionDto> restaurantOptions;
}