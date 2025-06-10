package com.example.thesimpleeventapp.dto.user;

import com.example.thesimpleeventapp.model.Event;
import com.example.thesimpleeventapp.model.Notification;
import com.example.thesimpleeventapp.model.Role;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private Role role;
    private List<Event> eventsCreated;
    private List<Notification> notifications;

}
