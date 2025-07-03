package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.message.CreateMessageDto;
import com.example.thesimpleeventapp.dto.message.MessageDto;
import com.example.thesimpleeventapp.service.chat.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final ChatService chatService;

    public WebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.sendMessage")
    public MessageDto sendMessage(@Payload CreateMessageDto createMessageDto) {
        System.out.println("Received message text: " + createMessageDto.getText());
        return chatService.sendMessage(createMessageDto);
    }
}
