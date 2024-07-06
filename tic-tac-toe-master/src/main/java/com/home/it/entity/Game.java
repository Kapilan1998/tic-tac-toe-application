package com.home.it.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table
@Data
public class Game {
    @Id
    private String id;
    private LocalDateTime gameDate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GameResult> gameResults;
}
