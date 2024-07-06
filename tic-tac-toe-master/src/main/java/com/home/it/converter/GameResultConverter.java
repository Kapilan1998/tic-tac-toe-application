package com.home.it.converter;

import com.home.it.entity.GameResult;
import com.home.it.entity.Player;
import com.home.it.exception.ServiceException;
import com.home.it.model.TicTacToe;
import com.home.it.repository.GameRepository;
import com.home.it.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameResultConverter {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameResult convertToEntity(TicTacToe ticTacToeDto) {
        GameResult gameResult = new GameResult();
        gameResult.setGame(gameRepository.findById(ticTacToeDto.getGameId()).orElseThrow(() -> new ServiceException("Game id not found", "Bad request", HttpStatus.BAD_REQUEST)));
        String winnerName = ticTacToeDto.getWinner();
        Player player = playerRepository.findWinnerId(winnerName);
        gameResult.setPlayerId(player);
        return gameResult;
    }
}
