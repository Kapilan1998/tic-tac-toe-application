package com.home.it.service;

import com.home.it.entity.Game;
import com.home.it.model.TicTacToe;
import com.home.it.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }
}
