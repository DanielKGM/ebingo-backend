package br.danielkgm.ebingo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.danielkgm.ebingo.model.Card;

public interface CardRepo extends JpaRepository<Card, String> {
}
