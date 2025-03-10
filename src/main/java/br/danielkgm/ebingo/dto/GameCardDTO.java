package br.danielkgm.ebingo.dto;

import java.time.LocalDateTime;

import br.danielkgm.ebingo.enumm.GameStatus;
import br.danielkgm.ebingo.model.Game;

public record GameCardDTO(
        String id,
        String roomName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Boolean prize,
        Integer cardSize,
        Integer players,
        boolean manualFill,
        GameStatus status) {

    public static GameCardDTO fromModel(Game model) {
        Integer players = model.getPlayers() != null ? model.getPlayers().size() : null;
        Boolean prize = model.getPrize() != null && !model.getPrize().isEmpty();
        return new GameCardDTO(model.getId(), model.getRoomName(), model.getStartTime(), model.getEndTime(),
                prize, model.getCardSize(), players, model.isManualFill(), model.getStatus());
    }
}