package com.home.it.agent;

import com.home.it.converter.GameResultConverter;
import com.home.it.entity.GameResult;
import com.home.it.model.TicTacToe;
import com.home.it.service.GameResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameResultAgent {
    private final GameResultConverter gameResultConverter;
    private final GameResultService gameResultService;

    public GameResult saveGameResult(TicTacToe ticTacToeDto){
        GameResult gameResult = gameResultConverter.convertToEntity(ticTacToeDto);
        return gameResultService.saveGameResult(gameResult);
    }
}
