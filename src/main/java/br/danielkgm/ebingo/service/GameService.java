package br.danielkgm.ebingo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

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
    private final TokenService tokenService;
    private final Random random = new Random();
    private static final int DRAW_RANGE = 60;

    public GameService(GameRepo gameRepository, UserRepo userRepository, CardRepo cardRepository,
            TokenService tokenService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.tokenService = tokenService;
    }

    public boolean isTokenFromUser(String token, String userId) {
        String nickname = tokenService.validateToken(token);
        Optional<User> foundUser = userRepository.findById(userId);
        String foundNickname = foundUser.isPresent() ? foundUser.get().getNickname() : null;
        return (foundNickname != null && foundNickname.equals(nickname));
    }

    public Game createGame(Game game) {
        game.setStatus(GameStatus.NAO_INICIADO);
        return gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Game> getGamesByRoomNameAndStatus(String roomName, GameStatus status) {
        return gameRepository.findByRoomNameContainingIgnoreCaseAndStatus(roomName, status);
    }

    public Card getUserCard(String gameId, String userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return cardRepository.findByGameAndUser(game, user)
                .orElseThrow(() -> new RuntimeException("Gere uma cartela para entrar no jogo"));

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
        return gameRepository.save(game);
    }

    public void deleteGame(String id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        gameRepository.delete(game);
    }

    public Game getGameById(String id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Card joinGame(String gameId, String userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Gera os números da cartela
        List<Integer> cardNumbers = generateCardNumbers(game.getCardSize());

        Card card = new Card();
        card.setGame(game);
        card.setUser(user);
        card.setNumbers(cardNumbers);

        cardRepository.save(card);

        game.getPlayers().add(user);
        gameRepository.save(game);

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
        return gameRepository.save(game);
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

        // Adiciona o número aos números marcados
        if (!card.getMarkedNumbers().contains(number)) {
            card.getMarkedNumbers().add(number);
            cardRepository.save(card);
        }

        // Verificar se a cartela foi totalmente marcada -> vitória
        if (card.getMarkedNumbers().containsAll(card.getNumbers())) {
            game.setWinner(user);
            gameRepository.save(game);
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

        return "Parabéns! Você ganhou: " + game.getPrize();
    }

    private List<Integer> generateCardNumbers(int size) {
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() < size) {
            numbers.add(this.random.nextInt(DRAW_RANGE) + 1);
        }
        return new ArrayList<>(numbers);
    }
}
