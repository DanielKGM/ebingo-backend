package br.danielkgm.ebingo.dto;

import br.danielkgm.ebingo.enumm.GameStatus;

public record GameFilterDTO(String roomName, GameStatus status) {
}
