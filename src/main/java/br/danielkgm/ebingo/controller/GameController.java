package br.danielkgm.ebingo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.danielkgm.ebingo.dto.GameCardDTO;
import br.danielkgm.ebingo.dto.GameFilterDTO;
import br.danielkgm.ebingo.model.Card;
import br.danielkgm.ebingo.model.Game;
import br.danielkgm.ebingo.service.GameService;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Criar um novo jogo (somente ADMIN pode criar)
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game createdGame = gameService.createGame(game);
        return new ResponseEntity<>(createdGame, HttpStatus.CREATED);
    }

    // Obter jogos filtrados
    @GetMapping("")
    public ResponseEntity<List<GameCardDTO>> getGamesByFilter(@ModelAttribute GameFilterDTO filter) {

        if (filter == null) {
            return new ResponseEntity<>(gameService.getAllGames(), HttpStatus.OK);
        }

        return new ResponseEntity<>(gameService.getGamesByFilter(filter), HttpStatus.OK);
    }

    // Atualizar um jogo (somente ADMIN pode editar)
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable String id, @RequestBody Game gameDetails) {
        Game updatedGame = gameService.updateGame(id, gameDetails);
        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    }

    // Deletar um jogo (somente ADMIN pode deletar)
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para buscar um jogo pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Se não encontrado, retorna 404
        }
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<Card> joinGame(@PathVariable String gameId, @RequestParam String userId) {
        return ResponseEntity.ok(gameService.joinGame(gameId, userId));
    }

    @PostMapping("/{gameId}/draw")
    public ResponseEntity<Game> drawNumber(@PathVariable String gameId) {
        return ResponseEntity.ok(gameService.addDrawnNumber(gameId));
    }

    @PostMapping("/{gameId}/mark")
    public ResponseEntity<List<Integer>> markNumber(@PathVariable String gameId, @RequestParam String userId,
            @RequestParam int number) {
        return ResponseEntity.ok(gameService.markNumber(gameId, userId, number));
    }

    @GetMapping("/{gameId}/prize")
    public ResponseEntity<String> getPrize(@PathVariable String gameId, @RequestParam String userId) {
        return ResponseEntity.ok(gameService.getPrize(gameId, userId));
    }

    @GetMapping("/{gameId}/{userId}")
    public ResponseEntity<Card> getUserCard(
            @PathVariable String gameId,
            @PathVariable String userId) {
        return ResponseEntity.ok(gameService.getUserCard(gameId, userId));
    }
}
