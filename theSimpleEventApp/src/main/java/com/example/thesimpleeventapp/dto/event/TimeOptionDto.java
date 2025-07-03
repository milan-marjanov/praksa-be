package com.example.thesimpleeventapp.dto.event;

import com.example.thesimpleeventapp.dto.user.UserProfileDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeOptionDto {
    private Long id;
    private Integer maxCapacity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private Integer votesCount;
    private Integer reservedCount;
    private List<UserProfileDto> votedUsers;
}