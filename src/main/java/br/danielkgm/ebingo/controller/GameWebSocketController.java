package br.danielkgm.ebingo.controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import br.danielkgm.ebingo.model.Game;

@Controller
public class GameWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public GameWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGameUpdate(Game game) {
        messagingTemplate.convertAndSend("/topic/game/" + game.getId(), game);
    }
}
