package com.example.thesimpleeventapp.dto.mapper;

import com.example.thesimpleeventapp.dto.event.ParticipantDto;
import com.example.thesimpleeventapp.dto.user.UserMessageDto;
import com.example.thesimpleeventapp.dto.user.UserRequestDto;
import com.example.thesimpleeventapp.model.User;

public class UserMapper {
    public static ParticipantDto participantToDto(User user) {
        ParticipantDto dto = new ParticipantDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfilePicture(user.getProfilePictureUrl());
        return dto;
    }

    public static UserRequestDto userRequestToDto(User user) {
        UserRequestDto dto = new UserRequestDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setProfilePicture(user.getProfilePictureUrl());
        dto.setRole(user.getRole());
        return dto;
    }

    public static UserMessageDto toUserMessageDto(User user) {
        UserMessageDto dto = new UserMessageDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}