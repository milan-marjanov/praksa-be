package com.example.thesimpleeventapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToMany()
    private List<User> participants;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<TimeOption> timeOptions;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<RestaurantOption> restaurantOptions;

    @OneToOne(cascade = CascadeType.ALL)
    private Chat chat;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Vote> votes;

}
