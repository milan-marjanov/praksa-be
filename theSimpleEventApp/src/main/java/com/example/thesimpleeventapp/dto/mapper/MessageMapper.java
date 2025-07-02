package com.example.thesimpleeventapp.dto.mapper;
import com.example.thesimpleeventapp.dto.message.MessageDto;
import com.example.thesimpleeventapp.model.Message;
import static com.example.thesimpleeventapp.dto.mapper.UserMapper.toUserMessageDto;

public class MessageMapper {
    public static MessageDto toMessageDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .text(message.getText())
                .sentAt(message.getSentAt())
                .user(toUserMessageDto(message.getUser()))
                .repliedToMessage(null)
                .chatId(message.getChat().getId())
                .build();
    }
}
