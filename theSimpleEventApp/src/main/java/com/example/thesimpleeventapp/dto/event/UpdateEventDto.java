package com.example.thesimpleeventapp.dto.event;

import com.example.thesimpleeventapp.model.RestaurantOption;
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

    private List<TimeOptionDto> timeOptions;

    private List<RestaurantOptionDto> restaurantOptions;

    private LocalDateTime votingDeadline;
}
