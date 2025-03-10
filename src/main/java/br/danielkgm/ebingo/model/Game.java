package br.danielkgm.ebingo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import br.danielkgm.ebingo.enumm.GameStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String roomName;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private String prize;

    @Column
    private Integer cardSize;

    @Column(nullable = false)
    private boolean manualFill;

    @ElementCollection
    @CollectionTable(name = "drawn", joinColumns = @JoinColumn(name = "user_id"))
    private List<Integer> drawnNumbers;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @ManyToMany
    @JoinTable(name = "game_participation", joinColumns = @JoinColumn(name = "game_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> players;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(nullable = false, updatable = false, name = "created_by")
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false, name = "modified_by")
    private String lastModifiedBy;
}
