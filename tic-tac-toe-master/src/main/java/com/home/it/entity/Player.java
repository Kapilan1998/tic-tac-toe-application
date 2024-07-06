package com.home.it.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstPlayerName;
    private String secondPlayerName;
}
