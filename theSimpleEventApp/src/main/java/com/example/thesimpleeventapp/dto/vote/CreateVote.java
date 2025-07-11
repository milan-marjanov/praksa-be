package com.example.thesimpleeventapp.dto.vote;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateVote {

    @NotNull(message = "Event ID must not be null")
    private Long eventId;

    private Long timeOptionId;

    private Long restaurantOptionId;

}
