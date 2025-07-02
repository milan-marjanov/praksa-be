package com.example.thesimpleeventapp.dto.chat;

import com.example.thesimpleeventapp.dto.message.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

     private Long id;

     private Long eventId;

     private List<MessageDto> messages;
}
