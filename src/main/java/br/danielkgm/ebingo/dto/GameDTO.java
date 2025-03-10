package br.danielkgm.ebingo.dto;

import java.time.LocalDateTime;
import java.util.List;

import br.danielkgm.ebingo.enumm.GameStatus;
import br.danielkgm.ebingo.model.Game;

public record GameDTO(
        String id,
        String roomName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean manualFill,
        List<Integer> drawnNumbers,
        GameStatus status,
        UserDTO winner,
        Integer cardSize) {

    public static GameDTO fromModel(Game game) {
        if (game == null) {
            return null;
        }

        return new GameDTO(
                game.getId(),
                game.getRoomName(),
                game.getStartTime(),
                game.getEndTime(),
                game.isManualFill(),
                game.getDrawnNumbers(),
                game.getStatus(),
                UserDTO.fromModel(game.getWinner()),
                game.getCardSize());
    }
}
