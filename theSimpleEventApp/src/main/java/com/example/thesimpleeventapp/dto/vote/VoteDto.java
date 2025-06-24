package com.example.thesimpleeventapp.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {
    private Long id;
    private Long eventId;
    private Long userId;
    private Long timeOptionId;
    private Long restaurantOptionId;
}
