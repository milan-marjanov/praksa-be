package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.vote.VoteDto;
import com.example.thesimpleeventapp.model.Vote;

public class VoteMapper {
    public static VoteDto toDto(Vote v) {
        return VoteDto.builder()
                .id(v.getId())
                .eventId(v.getEvent().getId())
                .userId(v.getUser().getId())
                .timeOptionId(v.getTimeOption() != null ? v.getTimeOption().getId() : null)
                .restaurantOptionId(v.getRestaurantOption() != null ? v.getRestaurantOption().getId() : null)
                .build();
    }
}
