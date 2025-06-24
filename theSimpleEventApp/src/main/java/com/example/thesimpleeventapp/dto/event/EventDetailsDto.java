package com.example.thesimpleeventapp.dto.event;


import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import com.example.thesimpleeventapp.dto.vote.VoteDto;
import com.example.thesimpleeventapp.model.RestaurantOptionType;
import com.example.thesimpleeventapp.model.TimeOptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    private LocalDateTime votingDeadline;

    private List<TimeOptionDto> timeOptions;

    private List<RestaurantOptionDto> restaurantOptions;

    private TimeOptionType timeOptionType;

    private RestaurantOptionType restaurantOptionType;

    private VoteDto currentVote;
}
