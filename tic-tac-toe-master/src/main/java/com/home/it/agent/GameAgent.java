package com.home.it.agent;

import com.home.it.converter.GameConverter;
import com.home.it.entity.Game;
import com.home.it.model.TicTacToe;
import com.home.it.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameAgent {
    private final GameService gameService;
    private final GameConverter gameConverter;
    public Game saveGame(TicTacToe ticTacToeDto) {
        Game game = gameConverter.convertToEntity(ticTacToeDto);
        return gameService.saveGame(game);
    }
}
