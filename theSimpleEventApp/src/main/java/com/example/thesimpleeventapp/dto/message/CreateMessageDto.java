package com.example.thesimpleeventapp.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageDto {

    @NotBlank(message = "Message text must not be blank.")
    private String text;

    @NotNull
    private Long userId;

    private Long repliedToMessageId;

    @NotNull
    private Long chatId;
}
