package com.example.thesimpleeventapp.dto.message;

import com.example.thesimpleeventapp.dto.user.UserMessageDto;
import com.example.thesimpleeventapp.model.Chat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDto {

    private long id;

    private String text;

    private LocalDateTime sentAt;

    private UserMessageDto user;

    private MessageDto repliedToMessage;

    private Chat chat;

}
