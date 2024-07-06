package com.home.it.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    private String winnerName;

}
