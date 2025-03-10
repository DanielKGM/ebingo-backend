package br.danielkgm.ebingo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.danielkgm.ebingo.audit.GameAudit;
import br.danielkgm.ebingo.model.Game;

public interface GameAuditRepo extends JpaRepository<GameAudit, String> {
    List<GameAudit> findByGameOrderByTimestampDesc(Game game);
}
