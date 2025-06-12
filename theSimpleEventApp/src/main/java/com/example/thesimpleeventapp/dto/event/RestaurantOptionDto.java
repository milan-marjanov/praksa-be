package com.example.thesimpleeventapp.dto.event;


import lombok.*;

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
}
