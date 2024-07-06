package com.home.it.controller;

import com.home.it.agent.GameAgent;
import com.home.it.entity.Game;
import com.home.it.model.TicTacToe;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/game")
@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameAgent gameAgent;
    @PostMapping("/save")
    public Game saveGame(@RequestBody TicTacToe ticTacToe){
        return gameAgent.saveGame(ticTacToe);
    }
}
