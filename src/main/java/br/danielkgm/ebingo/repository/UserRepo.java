package br.danielkgm.ebingo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.danielkgm.ebingo.model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}