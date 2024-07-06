package com.home.it.controller;

import com.home.it.agent.GameAgent;
import com.home.it.agent.GameResultAgent;
import com.home.it.agent.PlayerAgent;
import com.home.it.enums.GameState;
import com.home.it.manager.TicTacToeManager;
import com.home.it.model.TicTacToe;
import com.home.it.model.dto.JoinMessage;
import com.home.it.model.dto.PlayerMessage;
import com.home.it.model.dto.TicTacToeMessage;
import com.home.it.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@Slf4j
public class MessageController {

    @Autowired
    MessageService messageService;

    @MessageMapping("/game.join")
    @SendTo("/topic/game.state")        //all subscribers to /topic/game.state will receive the message.
    public Object joinGame(@Payload JoinMessage message, SimpMessageHeaderAccessor headerAccessor) {
        return messageService.joinGame(message, headerAccessor);
    }


    @MessageMapping("/game.leave")
    public void leaveGame(@Payload PlayerMessage message) {
        messageService.leaveGame(message);
    }


    @MessageMapping("/game.move")
    public void makeMove(@Payload TicTacToeMessage message) {
        messageService.makeMove(message);
    }

    @EventListener
    public void SessionDisconnectEvent(SessionDisconnectEvent event) {
        messageService.sessionDisconnectEvent(event);
    }


}
