package br.danielkgm.ebingo.dto;

import br.danielkgm.ebingo.enumm.Role;
import br.danielkgm.ebingo.model.User;

public record UserDTO(
        String id,
        String nickname,
        String password,
        String email,
        Role role) {

    public static User toModel(UserDTO dto) {
        User user = new User();
        if (dto == null) {
            return user;
        }
        user.setId(dto.id());
        user.setNickname(dto.nickname());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(dto.role() != null ? dto.role() : Role.USER);
        return user;
    }

    public static UserDTO fromModel(User user) {
        if (user == null) {
            return new UserDTO(null, null, null, null, null);
        }

        return new UserDTO(
                user.getId(),
                user.getNickname(),
                null,
                user.getEmail(),
                user.getRole());
    }
}
