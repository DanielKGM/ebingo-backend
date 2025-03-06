package br.danielkgm.ebingo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.danielkgm.ebingo.dto.UserDTO;
import br.danielkgm.ebingo.service.AuthService;

record LoginRequest(String email, String password) {
}

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        UserDTO resposta = UserDTO.fromModel(authService.register(userDTO));
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return authService.login(request.email(), request.password())
                .map(user -> ResponseEntity.ok("Autenticado com sucesso!"))
                .orElse(ResponseEntity.status(401).body("Credenciais inválidas!"));
    }
}
