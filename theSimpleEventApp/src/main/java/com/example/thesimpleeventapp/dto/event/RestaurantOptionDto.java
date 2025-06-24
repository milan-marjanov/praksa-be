package com.example.thesimpleeventapp.dto.event;


import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantOptionDto {
    private Long id;
    private String name;
    private String menuImageUrl;
    private String restaurantUrl;
    private Integer votesCount;
    private List<UserProfileDto> votedUsers;
}
