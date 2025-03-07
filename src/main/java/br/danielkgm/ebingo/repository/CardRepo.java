package br.danielkgm.ebingo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.danielkgm.ebingo.model.Card;
import br.danielkgm.ebingo.model.Game;
import br.danielkgm.ebingo.model.User;

public interface CardRepo extends JpaRepository<Card, String> {
    Optional<Card> findByGameAndUser(Game game, User user);
}
