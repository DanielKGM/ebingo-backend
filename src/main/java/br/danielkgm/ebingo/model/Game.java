package br.danielkgm.ebingo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import br.danielkgm.ebingo.enumm.GameStatus;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
