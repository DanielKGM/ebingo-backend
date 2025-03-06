package br.danielkgm.ebingo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import br.danielkgm.ebingo.dto.LoginResDTO;
import br.danielkgm.ebingo.dto.LoginReqDTO;
import br.danielkgm.ebingo.dto.UserDTO;
import br.danielkgm.ebingo.model.User;
import br.danielkgm.ebingo.service.AuthService;
import br.danielkgm.ebingo.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager,
            TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        UserDTO resposta = UserDTO.fromModel(authService.register(userDTO));
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO request) {
        var nickPassword = new UsernamePasswordAuthenticationToken(request.nickname(), request.password());
        var auth = this.authenticationManager.authenticate(nickPassword);
        User user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResDTO(token, UserDTO.fromModel(user)));
    }
}
