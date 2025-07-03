package com.example.thesimpleeventapp.dto.timeOptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TimeOptionDto {

    private long id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime votingDeadline;
}
