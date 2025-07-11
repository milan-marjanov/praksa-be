package com.example.thesimpleeventapp.dto.event;

import com.example.thesimpleeventapp.model.RestaurantOption;
import com.example.thesimpleeventapp.model.RestaurantOptionType;
import com.example.thesimpleeventapp.model.TimeOptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventDto {

    private String title;

    private String description;

    private List<Long> participantIds;

    private LocalDateTime votingDeadline;

    private List<TimeOptionDto> timeOptions;

    private TimeOptionType timeOptionType;

    private List<RestaurantOptionDto> restaurantOptions;

    private RestaurantOptionType restaurantOptionType;
}