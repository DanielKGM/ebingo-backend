package br.danielkgm.ebingo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.danielkgm.ebingo.dto.UpdtResDTO;
import br.danielkgm.ebingo.dto.UserDTO;
import br.danielkgm.ebingo.model.User;
import br.danielkgm.ebingo.service.TokenService;
import br.danielkgm.ebingo.service.UserService;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PutMapping("/update")
    public ResponseEntity<UpdtResDTO> updateUser(@RequestBody UserDTO userDTO,
            @RequestHeader("Authorization") String token) {
        User updatedUser = userService.updateUser(userDTO, token);
        UserDTO dto = UserDTO.fromModel(updatedUser);
        return ResponseEntity.ok(new UpdtResDTO(tokenService.generateToken(updatedUser), dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String token) {
        userService.deleteUser(token);
        return ResponseEntity.noContent().build();
    }
}
