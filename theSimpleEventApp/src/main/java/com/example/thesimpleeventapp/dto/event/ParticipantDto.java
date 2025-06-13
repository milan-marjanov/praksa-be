package com.example.thesimpleeventapp.dto.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipantDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String profilePicture;
}
