package com.example.thesimpleeventapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    private Integer maxCapacity;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id")

    private Event event;

    @OneToMany(mappedBy = "timeOption", cascade = CascadeType.ALL)
    private List<Vote> votes;

}
