package com.example.thesimpleeventapp.dto.event;


import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDetailsDto {

    private long id;

    private String title;

    private String description;

    private List<UserProfileDto> participants;

    private List<TimeOptionDto> timeOptions;

    private List<RestaurantOptionDto> restaurantOptions;
}
