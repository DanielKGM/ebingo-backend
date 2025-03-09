package br.danielkgm.ebingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.danielkgm.ebingo.dto.GameFilterDTO;
import br.danielkgm.ebingo.enumm.GameStatus;
import br.danielkgm.ebingo.model.Game;

public interface GameRepo extends JpaRepository<Game, String> {

    @Query("SELECT g FROM Game g " +
            "WHERE (:#{#filter.roomName} IS NULL OR LOWER(g.roomName) LIKE LOWER(CONCAT('%', :#{#filter.roomName}, '%'))) "
            +
            "AND (:#{#filter.status} IS NULL OR g.status = :#{#filter.status}) " +
            "ORDER BY g.startTime DESC")
    List<Game> findByFilter(@Param("filter") GameFilterDTO filter);

    List<Game> findByRoomNameContainingIgnoreCase(String roomName);

    List<Game> findByStatus(GameStatus status);
}
