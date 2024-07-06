package com.home.it.manager;

import com.home.it.enums.GameState;
import com.home.it.model.TicTacToe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TicTacToeManager {


     private final Map<String, TicTacToe> games;        // Map of active Tic-Tac-Toe games, with the game ID as the key.
    protected final Map<String, String> waitingPlayers; // Map of players waiting to join a Tic-Tac-Toe game, with the player's name as the key.

    public TicTacToeManager() {
        games = new ConcurrentHashMap<>();
        waitingPlayers = new ConcurrentHashMap<>();
    }

    public synchronized TicTacToe joinGame(String player) {
        if (games.values().stream().anyMatch(game -> game.getPlayer1().equals(player) || (game.getPlayer2() != null && game.getPlayer2().equals(player)))) {
            return games.values().stream().filter(game -> game.getPlayer1().equals(player) || game.getPlayer2().equals(player)).findFirst().get();
        }

        for (TicTacToe game : games.values()) {
            if (game.getPlayer1() != null && game.getPlayer2() == null) {
                game.setPlayer2(player);
                game.setGameState(GameState.PLAYER1_TURN);
                return game;
            }
        }

        TicTacToe game = new TicTacToe(player, null);
        games.put(game.getGameId(), game);
        waitingPlayers.put(player, game.getGameId());
        return game;
    }


    public synchronized TicTacToe leaveGame(String player) {
        String gameId = getGameByPlayer(player) != null ? getGameByPlayer(player).getGameId() : null;
        if (gameId != null) {
            waitingPlayers.remove(player);
            TicTacToe game = games.get(gameId);
            //if player 1 leaves game
            if (player.equals(game.getPlayer1())) {
                if (game.getPlayer2() != null) {
                    game.setPlayer1(game.getPlayer2());
                    game.setPlayer2(null);
                    game.setGameState(GameState.WAITING_FOR_PLAYER);
                    game.setBoard(new String[3][3]);
                    waitingPlayers.put(game.getPlayer1(), game.getGameId());
                } else {
                    games.remove(gameId);
                    return null;
                }
            } else if (player.equals(game.getPlayer2())) {
                game.setPlayer2(null);
                game.setGameState(GameState.WAITING_FOR_PLAYER);
                game.setBoard(new String[3][3]);
                waitingPlayers.put(game.getPlayer1(), game.getGameId());
            }
            return game;
        }
        return null;
    }


    public TicTacToe getGame(String gameId) {
        return games.get(gameId);
    }


    // either player1 or player2 present
    public TicTacToe getGameByPlayer(String player) {
        return games.values().stream().filter(game -> game.getPlayer1().equals(player) || (game.getPlayer2() != null &&
                game.getPlayer2().equals(player))).findFirst().orElse(null);
    }


    public void removeGame(String gameId) {
        games.remove(gameId);
    }
}
