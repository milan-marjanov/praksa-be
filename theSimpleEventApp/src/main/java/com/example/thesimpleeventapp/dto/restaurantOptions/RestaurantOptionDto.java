package com.example.thesimpleeventapp.dto.restaurantOptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantOptionDto {

    private long id;

    private String name;

    private String menuImageUrl;

    private String restaurantUrl;
}
