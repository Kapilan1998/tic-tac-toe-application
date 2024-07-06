package com.home.it.converter;

import com.home.it.entity.GameResult;
import com.home.it.entity.Player;
import com.home.it.exception.ServiceException;
import com.home.it.model.TicTacToe;
import com.home.it.model.dto.GameResultDto;
import com.home.it.repository.GameRepository;
import com.home.it.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameResultConverter {
    private final GameRepository gameRepository;

    public GameResult convertToEntity(TicTacToe ticTacToeDto) {
        GameResult gameResult = new GameResult();
        gameResult.setGame(gameRepository.findById(ticTacToeDto.getGameId()).orElseThrow(() -> new ServiceException("Game id not found", "Bad request", HttpStatus.BAD_REQUEST)));
        gameResult.setWinnerName(ticTacToeDto.getWinner());
        return gameResult;
    }

    public GameResultDto convertToDto(GameResult gameResult) {
        GameResultDto gameResultDto = new GameResultDto();
        gameResultDto.setGameName(gameResult.getGame().getGameName());
        gameResultDto.setWinner(gameResult.getWinnerName());

//        BeanUtils.copyProperties(gameResult, gameResultDto);
//        if (gameResult.getGame() != null) {
//            gameResultDto.setGameName(gameResult.getGame().getGameName());
//        }

        return gameResultDto;
    }
}
