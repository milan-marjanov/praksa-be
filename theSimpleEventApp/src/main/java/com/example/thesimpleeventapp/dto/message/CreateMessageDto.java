package com.example.thesimpleeventapp.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateMessageDto {

    @NotBlank(message = "Message text must not be blank.")
    private String text;

    @NotNull
    private Long userId;

    private Long repliedToMessageId;

    @NotNull
    private Long chatId;
}
