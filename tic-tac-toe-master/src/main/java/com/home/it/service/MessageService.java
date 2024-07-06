package com.home.it.service;

import com.home.it.agent.GameAgent;
import com.home.it.agent.GameResultAgent;
import com.home.it.agent.PlayerAgent;
import com.home.it.enums.GameState;
import com.home.it.manager.TicTacToeManager;
import com.home.it.model.TicTacToe;
import com.home.it.model.dto.JoinMessage;
import com.home.it.model.dto.PlayerMessage;
import com.home.it.model.dto.TicTacToeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final TicTacToeManager ticTacToeManager = new TicTacToeManager();
    private final GameAgent gameAgent;
    private final PlayerAgent playerAgent;
    private final GameResultAgent gameResultAgent;
    private final SimpMessagingTemplate simpMessagingTemplate;      //    //Template for sending messages to web socket clients through the message broker.


    @Transactional
    public Object joinGame(JoinMessage message, SimpMessageHeaderAccessor headerAccessor) {
        TicTacToe game = ticTacToeManager.joinGame(message.getPlayer());
        if (game == null) {
            TicTacToeMessage errorMessage = new TicTacToeMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Unable to enter the game.");
            return errorMessage;
        }
        headerAccessor.getSessionAttributes().put("gameId", game.getGameId());
        log.info("game id = {}", game.getGameId());
        headerAccessor.getSessionAttributes().put("player", message.getPlayer());
        log.info("message.getPlayer() {}", message.getPlayer());

        TicTacToeMessage gameMessage = gameToMessage(game);
        gameMessage.setType("game.joined");
        //All subscribers to this topic will receive msg
        return gameMessage;
    }


    private TicTacToeMessage gameToMessage(TicTacToe game) {
        TicTacToeMessage message = new TicTacToeMessage();
        message.setGameId(game.getGameId());
        message.setPlayer1(game.getPlayer1());
        message.setPlayer2(game.getPlayer2());
        message.setBoard(game.getBoard());
        message.setTurn(game.getTurn());
        message.setGameState(game.getGameState());
        message.setWinner(game.getWinner());
        log.info("game details {},", game);
        gameAgent.saveGame(game);
        if(game.getWinner() != null) {
            playerAgent.savePlayer(game);
            gameResultAgent.saveGameResult(game);
        }
        return message;
    }

    public void leaveGame(PlayerMessage message) {
        TicTacToe game = ticTacToeManager.leaveGame(message.getPlayer());
        if (game != null) {
            TicTacToeMessage gameMessage = gameToMessage(game);
            gameMessage.setType("game.left");
            // convert the payload to a messaging format so, below method used (first argument is who subscribed this will receive msg, second is message)
            simpMessagingTemplate.convertAndSend("/topic/game." + game.getGameId(), gameMessage);
        }
    }

    public void makeMove(TicTacToeMessage message) {
        String player = message.getSender();
        String gameId = message.getGameId();
        int move = message.getMove();
        TicTacToe game = ticTacToeManager.getGame(gameId);

        if (game == null || game.isGameOver()) {
            TicTacToeMessage errorMessage = new TicTacToeMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Game already finished");
            this.simpMessagingTemplate.convertAndSend("/topic/game." + gameId, errorMessage);
            return;
        }

        if (game.getGameState().equals(GameState.WAITING_FOR_PLAYER)) {
            TicTacToeMessage errorMessage = new TicTacToeMessage();
            errorMessage.setType("error");
            errorMessage.setContent("Game is waiting for another player to join.");
            this.simpMessagingTemplate.convertAndSend("/topic/game." + gameId, errorMessage);
            return;
        }

        if (game.getTurn().equals(player)) {
            game.makeMove(player, move);

            TicTacToeMessage gameStateMessage = new TicTacToeMessage(game);
            gameStateMessage.setType("game.move");
            this.simpMessagingTemplate.convertAndSend("/topic/game." + gameId, gameStateMessage);

            if (game.isGameOver()) {
                TicTacToeMessage gameOverMessage = gameToMessage(game);
                gameOverMessage.setType("game.gameOver");
                this.simpMessagingTemplate.convertAndSend("/topic/game." + gameId, gameOverMessage);
                ticTacToeManager.removeGame(gameId);
            }
        }
    }

    public void sessionDisconnectEvent(SessionDisconnectEvent event) {
        //Wraps the event message to access header attributes.
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String gameId = headerAccessor.getSessionAttributes().get("gameId").toString();
        String player = headerAccessor.getSessionAttributes().get("player").toString();
        TicTacToe game = ticTacToeManager.getGame(gameId);
        if (game != null) {
            if (game.getPlayer1().equals(player)) {
                game.setPlayer1(null);
                if (game.getPlayer2() != null) {
                    game.setGameState(GameState.PLAYER2_WON);
                    game.setWinner(game.getPlayer2());
                } else {
                    ticTacToeManager.removeGame(gameId);
                }
            } else if (game.getPlayer2() != null && game.getPlayer2().equals(player)) {
                game.setPlayer2(null);
                if (game.getPlayer1() != null) {
                    game.setGameState(GameState.PLAYER1_WON);
                    game.setWinner(game.getPlayer1());
                } else {
                    ticTacToeManager.removeGame(gameId);
                }
            }
            TicTacToeMessage gameMessage = gameToMessage(game);
            gameMessage.setType("game.gameOver");
            simpMessagingTemplate.convertAndSend("/topic/game." + gameId, gameMessage);
            ticTacToeManager.removeGame(gameId);
        }
    }
}
