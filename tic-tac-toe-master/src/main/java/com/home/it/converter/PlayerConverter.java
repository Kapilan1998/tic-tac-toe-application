package com.home.it.converter;

import com.home.it.entity.Player;
import com.home.it.model.TicTacToe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayerConverter {
    public Player convertToEntity(TicTacToe ticTacToeDto) {
        Player player = new Player();
        player.setFirstPlayerName(ticTacToeDto.getPlayer1());
        player.setSecondPlayerName(ticTacToeDto.getPlayer2());
        return player;
    }
}
