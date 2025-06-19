package com.example.thesimpleeventapp.dto.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventBasicDto {

    private Long id;

    private String title;

    private String description;
}
