package com.example.thesimpleeventapp.dto.message;

import com.example.thesimpleeventapp.dto.user.UserMessageDto;
import com.example.thesimpleeventapp.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private long id;

    private String text;

    private LocalDateTime sentAt;

    private UserMessageDto user;

    private MessageDto repliedToMessage;

    private long chatId;

}
