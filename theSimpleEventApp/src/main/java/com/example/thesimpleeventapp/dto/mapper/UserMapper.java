package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.event.ParticipantDto;
import com.example.thesimpleeventapp.model.User;

public class UserMapper {
    public static ParticipantDto toDto(User user) {
        ParticipantDto dto = new ParticipantDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfilePicture(user.getProfilePictureUrl());
        return dto;
    }
}
