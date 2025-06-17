package com.example.thesimpleeventapp.dto.event;


import com.example.thesimpleeventapp.model.RestaurantOption;
import com.example.thesimpleeventapp.model.TimeOption;
import com.example.thesimpleeventapp.model.TimeOptionType;
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
    private List<TimeOptionDto> timeOptions;
    private TimeOptionType timeOptionType;
    private List<RestaurantOption> restaurantOptions;
}
