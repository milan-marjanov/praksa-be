package com.example.thesimpleeventapp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageDto {

    private Long id;

    private String firstName;

    private String lastName;
}
