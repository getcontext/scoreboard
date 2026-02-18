package com.sportradar.scoreboard;

import java.time.Instant;
import java.util.Objects;

/**
 * Represents a single football match.
 * <p>
 * The class is immutable except for the mutable scores, which are updated
 * via {@link ScoreBoard#updateScore(String, String, int, int)}.
 */
public class Game {
    private final String homeTeam;
    private final String awayTeam;
    private int homeScore;
    private int awayScore;
    /** Moment when the game was added to the board */
    private final Instant createdAt;
    /** Unique, ever‑increasing insertion order (used for tie‑break). */
    private final long insertionOrder;

    public Game(String homeTeam, String awayTeam, long insertionOrder) {
        this.homeTeam = Objects.requireNonNull(homeTeam);
        this.awayTeam = Objects.requireNonNull(awayTeam);
        this.homeScore = 0;
        this.awayScore = 0;
        this.createdAt = Instant.now();
        this.insertionOrder = insertionOrder;
    }

    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public int getHomeScore() { return homeScore; }
    public int getAwayScore() { return awayScore; }
    public Instant getCreatedAt() { return createdAt; }
    public long getInsertionOrder() { return insertionOrder; }

    public void setScore(int homeScore, int awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public int totalScore() {
        return homeScore + awayScore;
    }

    /** Unique key used in the board’s map – order matters (home vs away). */
    public String key() {
        return homeTeam + "|" + awayTeam;
    }
}