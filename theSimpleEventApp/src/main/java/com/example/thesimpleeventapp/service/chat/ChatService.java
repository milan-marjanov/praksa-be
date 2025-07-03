package com.example.thesimpleeventapp.service.chat;

import com.example.thesimpleeventapp.dto.chat.ChatDto;
import com.example.thesimpleeventapp.dto.message.CreateMessageDto;
import com.example.thesimpleeventapp.dto.message.MessageDto;

public interface ChatService {
    MessageDto sendMessage(CreateMessageDto dto);
    ChatDto getChatByEvent(Long eventId);

    public ChatDto getChatByEventId(Long eventId);
}
