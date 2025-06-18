package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.event.RestaurantOptionDto;
import com.example.thesimpleeventapp.dto.event.TimeOptionDto;
import com.example.thesimpleeventapp.model.RestaurantOption;
import com.example.thesimpleeventapp.model.TimeOption;

public class RestaurantOptionMapper {
    public static RestaurantOptionDto toDto(RestaurantOption restaurantOption) {
        RestaurantOptionDto restaurantOptionDto = new RestaurantOptionDto();
        restaurantOptionDto.setId(restaurantOption.getId());
        restaurantOptionDto.setName(restaurantOption.getName());
        restaurantOptionDto.setMenuImageUrl(restaurantOption.getMenuImageUrl());
        restaurantOptionDto.setRestaurantUrl(restaurantOption.getRestaurantUrl());
        return restaurantOptionDto;
    }

    public static RestaurantOption toEntity(RestaurantOptionDto restaurantOptionDto) {
        RestaurantOption restaurantOption = new RestaurantOption();
        restaurantOption.setName(restaurantOptionDto.getName());
        restaurantOption.setMenuImageUrl(restaurantOptionDto.getMenuImageUrl());
        restaurantOption.setRestaurantUrl(restaurantOptionDto.getRestaurantUrl());
        return restaurantOption;
    }
}
