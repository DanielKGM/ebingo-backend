package br.danielkgm.ebingo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.danielkgm.ebingo.dto.UserDTO;
import br.danielkgm.ebingo.model.User;
import br.danielkgm.ebingo.repository.UserRepo;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepo userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserDTO userDTO) {
        User user = UserDTO.toModel(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByNickname(username);
    }
}
