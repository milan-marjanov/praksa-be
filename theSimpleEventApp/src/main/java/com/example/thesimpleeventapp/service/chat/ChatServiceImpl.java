package com.example.thesimpleeventapp.service.chat;

import com.example.thesimpleeventapp.dto.chat.ChatDto;
import com.example.thesimpleeventapp.dto.mapper.MessageMapper;
import com.example.thesimpleeventapp.dto.message.CreateMessageDto;
import com.example.thesimpleeventapp.dto.message.MessageDto;
import com.example.thesimpleeventapp.dto.user.UserMessageDto;
import com.example.thesimpleeventapp.model.Chat;
import com.example.thesimpleeventapp.model.Message;
import com.example.thesimpleeventapp.model.User;
import com.example.thesimpleeventapp.repository.ChatRepository;
import com.example.thesimpleeventapp.repository.MessageRepository;
import com.example.thesimpleeventapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageDto sendMessage(CreateMessageDto dto) {
        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Message repliedTo = null;
        if (dto.getRepliedToMessageId() != null) {
            repliedTo = messageRepository.findById(dto.getRepliedToMessageId())
                    .orElseThrow(() -> new EntityNotFoundException("Replied message not found"));
        }

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setText(dto.getText());
        message.setRepliedToMessage(repliedTo);
        message.setSentAt(LocalDateTime.now());

        Message saved = messageRepository.save(message);
        System.out.println("Saved message: " + saved.getText() + saved.getId());
        /*chat.getMessages().add(saved);
        chatRepository.save(chat);*/
        return MessageMapper.toMessageDto(saved);
    }

    @Override
    public ChatDto getChatByEvent(Long eventId) {
        Chat chat = chatRepository.findByEventId(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Chat for event not found"));

        List<MessageDto> messageDtos = chat.getMessages().stream()
                .map(MessageMapper::toMessageDto)
                .collect(toList());

        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setEventId(chat.getEvent().getId());
        dto.setMessages(messageDtos);

        return dto;
    }

    public ChatDto getChatByEventId(Long eventId) {
        Optional<Chat> chatOpt = chatRepository.findByEventId(eventId);
        if (chatOpt.isEmpty()) {
            return null;
        }

        Chat chat = chatOpt.get();

        ChatDto chatDto = new ChatDto();
        chatDto.setId(chat.getId());
        chatDto.setEventId(chat.getEvent().getId());

        List<MessageDto> messageDtos = chat.getMessages().stream()
                .map(MessageMapper::toMessageDto)
                .collect(Collectors.toList());

        chatDto.setMessages(messageDtos);
        return chatDto;
    }


}
