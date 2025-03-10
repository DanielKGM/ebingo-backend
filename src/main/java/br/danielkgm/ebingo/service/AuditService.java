package br.danielkgm.ebingo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import br.danielkgm.ebingo.audit.GameAudit;
import br.danielkgm.ebingo.enumm.GameAction;
import br.danielkgm.ebingo.model.Game;
import br.danielkgm.ebingo.repository.GameAuditRepo;

@Service
public class AuditService {

    private final GameAuditRepo gameAuditRepo;

    public AuditService(GameAuditRepo gameAuditRepo) {
        this.gameAuditRepo = gameAuditRepo;
    }

    public List<GameAudit> getAudit(Game game) {
        if (game == null) {
            return Collections.emptyList();
        }
        return gameAuditRepo.findByGameOrderByTimestampDesc(game);
    }

    public GameAudit createGameAudit(Game game, GameAction action) {
        if (game == null || action == null) {
            return null;
        }
        GameAudit ga = new GameAudit();
        ga.setGame(game);
        ga.setAction(action);
        return this.gameAuditRepo.save(ga);
    }
}
