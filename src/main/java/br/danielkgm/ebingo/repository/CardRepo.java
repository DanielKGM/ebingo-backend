package br.danielkgm.ebingo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.danielkgm.ebingo.model.Card;
import br.danielkgm.ebingo.model.Game;
import br.danielkgm.ebingo.model.User;

public interface CardRepo extends JpaRepository<Card, String> {
    Optional<Card> findByGameAndUser(Game game, User user);

    @Query("SELECT c FROM Card c WHERE c.game = :game ORDER BY SIZE(c.markedNumbers) DESC")
    List<Card> findByGame(@Param("game") Game game);
}
