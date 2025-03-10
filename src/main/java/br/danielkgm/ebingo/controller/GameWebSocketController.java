package br.danielkgm.ebingo.controller;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import br.danielkgm.ebingo.dto.RankingDTO;
import br.danielkgm.ebingo.model.Game;

@Controller
public class GameWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public GameWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGameUpdate(Game game) {
        messagingTemplate.convertAndSend("/topic/g/" + game.getId(), game);
    }

    public void sendRankingUpdate(List<RankingDTO> ranking, String gameId) {
        messagingTemplate.convertAndSend("/topic/g/" + gameId + "/r", ranking);
    }
}
