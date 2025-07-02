package com.example.thesimpleeventapp.controller;

import com.example.thesimpleeventapp.dto.chat.ChatDto;
import com.example.thesimpleeventapp.service.chat.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<ChatDto> getChatByEventId(@PathVariable Long eventId) {
        ChatDto chatDto = chatService.getChatByEventId(eventId);
        if (chatDto != null) {
            return ResponseEntity.ok(chatDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
