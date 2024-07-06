package com.home.it.agent;

import com.home.it.converter.PlayerConverter;
import com.home.it.entity.Player;
import com.home.it.model.TicTacToe;
import com.home.it.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerAgent {
    private final PlayerConverter playerConverter;
    private final PlayerService playerService;

    public Player savePlayer(TicTacToe ticTacToeDto) {
        Player player = playerConverter.convertToEntity(ticTacToeDto);
        return playerService.savePlayer(player);
    }
}
