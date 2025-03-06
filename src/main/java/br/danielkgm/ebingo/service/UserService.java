package br.danielkgm.ebingo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.danielkgm.ebingo.dto.UserDTO;
import br.danielkgm.ebingo.model.User;
import br.danielkgm.ebingo.repository.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepository;
    private final TokenService tokenService;

    public UserService(UserRepo userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public User updateUser(UserDTO userDTO, String token) {
        String nickname = tokenService.validateToken(token.replace("Bearer ", ""));
        User user = fetchUser(nickname);
        user.setNickname(userDTO.nickname());
        userRepository.save(user);

        return user;
    }

    public void deleteUser(String token) {
        String nickname = tokenService.validateToken(token.replace("Bearer ", ""));
        User user = fetchUser(nickname);
        userRepository.delete(user);
    }

    private User fetchUser(String nickName) {
        Optional<User> user = userRepository.findByNicknameOrderByNickname(nickName);
        if (user.isEmpty()) {
            new RuntimeException("Usuário não encontrado");
        }
        return user.get();
    }
}
