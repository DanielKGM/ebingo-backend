package br.danielkgm.ebingo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import br.danielkgm.ebingo.audit.GameAudit;
import br.danielkgm.ebingo.controller.GameWebSocketController;
import br.danielkgm.ebingo.dto.GameCardDTO;
import br.danielkgm.ebingo.dto.GameFilterDTO;
import br.danielkgm.ebingo.enumm.GameAction;
import br.danielkgm.ebingo.enumm.GameStatus;
import br.danielkgm.ebingo.model.Card;
import br.danielkgm.ebingo.model.Game;
import br.danielkgm.ebingo.model.User;
import br.danielkgm.ebingo.repository.CardRepo;
import br.danielkgm.ebingo.repository.GameRepo;
import br.danielkgm.ebingo.repository.UserRepo;

@Service
public class GameService {

    private final GameRepo gameRepository;
    private final UserRepo userRepository;
    private final CardRepo cardRepository;
    private final AuditService auditService;
    private final Random random = new Random();
    private static final int DRAW_RANGE = 60;
    private final GameWebSocketController gameWebSocketController;

    public GameService(GameRepo gameRepository, UserRepo userRepository, CardRepo cardRepository,
            AuditService auditService, GameWebSocketController gameWebSocketController) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.auditService = auditService;
        this.gameWebSocketController = gameWebSocketController;
    }

    public Game createGame(Game game) {
        game.setStatus(GameStatus.NAO_INICIADO);
        Game savedGame = gameRepository.save(game);
        this.auditService.createGameAudit(savedGame, GameAction.CREATED);
        return savedGame;
    }

    public List<GameAudit> getAudits(String uuid) {
        return this.auditService.getAudit(this.gameRepository.findById(uuid).orElse(null));
    }

    public List<GameCardDTO> getAllGames() {
        return toGameCardList(gameRepository.findAll());
    }

    public List<GameCardDTO> getGamesByFilter(GameFilterDTO filter) {
        List<Game> games = gameRepository.findByFilter(filter);
        return toGameCardList(games);
    }

    private List<GameCardDTO> toGameCardList(List<Game> games) {
        if (games == null) {
            return Collections.emptyList();
        }
        return games.stream().map(GameCardDTO::fromModel).toList();
    }

    public Card getUserCard(String gameId, String userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Optional<Card> card = cardRepository.findByGameAndUser(game, user);
        if (card.isEmpty()) {
            return new Card();
        }
        return card.get();

    }

    public Game updateGame(String id, Game gameDetails) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        game.setRoomName(gameDetails.getRoomName());
        game.setStartTime(gameDetails.getStartTime());
        game.setEndTime(gameDetails.getEndTime());
        game.setPrize(gameDetails.getPrize());
        game.setManualFill(gameDetails.isManualFill());
        game.setStatus(gameDetails.getStatus());
        game.setCardSize(gameDetails.getCardSize());
        this.auditService.createGameAudit(game, GameAction.EDITED);
        return saveAndSend(game);
    }

    private Game saveAndSend(Game game) {
        Game newGame = gameRepository.save(game);
        gameWebSocketController.sendGameUpdate(newGame);
        return newGame;
    }

    public Game getGameById(String id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Card joinGame(String gameId, String userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Jogo não encontrado!"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
        if (game.getPlayers().contains(user)) {
            throw new RuntimeException("Você já está participando deste jogo!");
        }
        // Gera os números da cartela
        List<Integer> cardNumbers = generateCardNumbers(game.getCardSize());

        Card card = new Card();
        card.setGame(game);
        card.setUser(user);
        card.setNumbers(cardNumbers);
        this.auditService.createGameAudit(game, GameAction.PLAYER_JOINED);
        cardRepository.save(card);
        game.getPlayers().add(user);
        saveAndSend(game);
        return card;
    }

    public Game addDrawnNumber(String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        if (game.getWinner() != null) {
            throw new RuntimeException("O jogo já tem um vencedor.");
        }

        if (game.getDrawnNumbers().size() >= DRAW_RANGE) {
            throw new RuntimeException("Todos os números já foram sorteados.");
        }

        Set<Integer> availableNumbers = IntStream.rangeClosed(1, DRAW_RANGE)
                .boxed()
                .collect(Collectors.toSet());
        availableNumbers.removeAll(game.getDrawnNumbers());

        List<Integer> availableList = new ArrayList<>(availableNumbers);
        int drawnNumber = availableList.get(this.random.nextInt(availableList.size()));

        game.getDrawnNumbers().add(drawnNumber);
        if (game.getDrawnNumbers().size() == 1) {
            this.auditService.createGameAudit(game, GameAction.STARTED);
        }
        this.auditService.createGameAudit(game, GameAction.NUMBER_DRAWN);
        return saveAndSend(game);
    }

    public List<Integer> markNumber(String gameId, String userId, int number) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Jogo não encontrado"));

        if (game.getWinner() != null) {
            throw new RuntimeException("O jogo já tem um vencedor.");
        }

        if (!game.getDrawnNumbers().contains(number)) {
            throw new RuntimeException("O número %d não foi sorteado!".formatted(number));
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Card card = cardRepository.findByGameAndUser(game, user)
                .orElseThrow(() -> new RuntimeException("Cartela não encontrada"));

        if (!card.getNumbers().contains(number)) {
            throw new RuntimeException("O número não está na cartela.");
        }

        if (!card.getMarkedNumbers().contains(number)) {
            card.getMarkedNumbers().add(number);
            this.auditService.createGameAudit(game, GameAction.NUMBER_MARKED);
            cardRepository.save(card);
        }

        if (card.getMarkedNumbers().containsAll(card.getNumbers())) {
            game.setWinner(user);
            this.auditService.createGameAudit(game, GameAction.GAME_WON);
            saveAndSend(game);
        }

        return card.getMarkedNumbers();
    }

    public String getPrize(String gameId, String userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (game.getWinner() == null) {
            return "O jogo ainda não tem um vencedor.";
        }

        if (!game.getWinner().equals(user)) {
            return "Você não é o vencedor.";
        }

        this.auditService.createGameAudit(game, GameAction.PRIZE_VIEWED);

        return game.getPrize();
    }

    private List<Integer> generateCardNumbers(int size) {
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() < size) {
            numbers.add(this.random.nextInt(DRAW_RANGE) + 1);
        }
        return new ArrayList<>(numbers);
    }
}
