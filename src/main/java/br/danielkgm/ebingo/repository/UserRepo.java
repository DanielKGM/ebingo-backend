package br.danielkgm.ebingo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import br.danielkgm.ebingo.model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);

    UserDetails findByNickname(String nickname);
}