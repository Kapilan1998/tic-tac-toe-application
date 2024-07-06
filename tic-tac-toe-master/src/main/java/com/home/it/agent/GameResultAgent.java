package com.home.it.agent;

import com.home.it.converter.GameResultConverter;
import com.home.it.entity.GameResult;
import com.home.it.model.TicTacToe;
import com.home.it.model.dto.GameResultDto;
import com.home.it.service.GameResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameResultAgent {
    private final GameResultConverter gameResultConverter;
    private final GameResultService gameResultService;

    public GameResult saveGameResult(TicTacToe ticTacToeDto){
        GameResult gameResult = gameResultConverter.convertToEntity(ticTacToeDto);
        return gameResultService.saveGameResult(gameResult);
    }

    public List<GameResultDto> showLastTenResult() {
        List<GameResultDto> gameResultDtos = new ArrayList<>();
        List<GameResult> gameResultList = gameResultService.showLastTenResults();
        log.info("gameResultList {}",gameResultList);
        GameResultDto gameResultDto = null;
        for(GameResult gameResult : gameResultList){
            gameResultDto = gameResultConverter.convertToDto(gameResult);
            gameResultDtos.add(gameResultDto);
        }
        return gameResultDtos;
    }
}
