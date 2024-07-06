package com.home.it.converter;

import com.home.it.entity.Game;
import com.home.it.entity.GameResult;
import com.home.it.model.TicTacToe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GameConverter {
    public Game convertToEntity(TicTacToe ticTacToeDto) {
        Game game = new Game();
        game.setId(ticTacToeDto.getGameId());
        game.setGameDate(LocalDateTime.now());
        game.setGameName("Triple game");
        return game;
    }
}
