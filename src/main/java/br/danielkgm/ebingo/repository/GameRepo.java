package br.danielkgm.ebingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.danielkgm.ebingo.enumm.GameStatus;
import br.danielkgm.ebingo.model.Game;

public interface GameRepo extends JpaRepository<Game, String> {

    List<Game> findByRoomNameContainingIgnoreCaseAndStatus(String roomName, GameStatus status);

    List<Game> findByRoomNameContainingIgnoreCase(String roomName);

    List<Game> findByStatus(GameStatus status);
}
