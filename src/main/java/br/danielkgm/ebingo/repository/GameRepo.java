package br.danielkgm.ebingo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.danielkgm.ebingo.model.Game;

public interface GameRepo extends JpaRepository<Game, String> {
}
