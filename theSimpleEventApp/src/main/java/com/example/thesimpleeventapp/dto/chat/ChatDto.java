package com.example.thesimpleeventapp.dto.chat;

import com.example.thesimpleeventapp.dto.message.MessageDto;
import lombok.Data;

import java.util.List;

@Data
public class ChatDto {

     private Long id;

     private Long eventId;

     private List<MessageDto> messages;




}
