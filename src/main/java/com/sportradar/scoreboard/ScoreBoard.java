package com.sportradar.scoreboard;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In‑memory scoreboard that supports the required operations:
 * <ul>
 *   <li>startGame</li>
 *   <li>finishGame</li>
 *   <li>updateScore</li>
 *   <li>getSummary</li>
 * </ul>
 *
 * The implementation follows the Single‑Responsibility Principle – it only
 * knows how to store, update and query {@link Game} objects.
 */
public class ScoreBoard {

    /**
     * LinkedHashMap preserves insertion order, which we need to resolve
     * ties when total scores are equal (most‑recently added wins).
     */
    private final Map<String, Game> games = new LinkedHashMap<>();

    /** Monotonic counter used to give each game a unique insertion order. */
    private long nextInsertionId = 0L;

    /** Starts a new match with a 0‑0 score. */
    public void startGame(String homeTeam, String awayTeam) {
        Objects.requireNonNull(homeTeam);
        Objects.requireNonNull(awayTeam);
        String key = makeKey(homeTeam, awayTeam);
        if (games.containsKey(key)) {
            throw new IllegalArgumentException(
                    String.format("Game %s vs %s already started.", homeTeam, awayTeam));
        }
        games.put(key, new Game(homeTeam, awayTeam, nextInsertionId++));
    }

    /** Finishes (removes) an existing match. */
    public void finishGame(String homeTeam, String awayTeam) {
        String key = makeKey(homeTeam, awayTeam);
        if (games.remove(key) == null) {
            throw new NoSuchElementException(
                    String.format("Game %s vs %s not found.", homeTeam, awayTeam));
        }
    }

    /**
     * Updates the score of a previously started match.
     *
     * @throws IllegalArgumentException if a score is negative
     * @throws NoSuchElementException   if the match does not exist
     */
    public void updateScore(String homeTeam, String awayTeam,
                            int homeScore, int awayScore) {
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores must be non‑negative integers.");
        }
        String key = makeKey(homeTeam, awayTeam);
        Game game = games.get(key);
        if (game == null) {
            throw new NoSuchElementException(
                    String.format("Game %s vs %s not found.", homeTeam, awayTeam));
        }
        game.setScore(homeScore, awayScore);
    }

    /**
     * Returns a summary list ordered by:
     * <ol>
     *   <li>Total score descending</li>
     *   <li>If total scores are equal – the most recently added game first</li>
     * </ol>
     *
     * Each entry is a {@link GameInfo} DTO that contains the four fields
     * requested by the consumer.
     */
    public List<GameInfo> getSummary() {
        List<GameInfo> collect = games.values().stream()
                .sorted(Comparator
                        .comparingInt(Game::totalScore).reversed()
                        .thenComparing(Comparator
                                .comparingLong(Game::getInsertionOrder)
                                .reversed()))
                .map(g -> new GameInfo(
                        g.getHomeTeam(), g.getHomeScore(),
                        g.getAwayTeam(), g.getAwayScore()))
                .collect(Collectors.toList());
        return collect;
    }

    /** Simple DTO returned by {@link #getSummary()}. */
    public static final class GameInfo {
        public String homeTeam;
        public int homeScore;
        public String awayTeam;
        public int awayScore;

        public GameInfo(String homeTeam, int homeScore,
                        String awayTeam, int awayScore) {
            this.homeTeam = homeTeam;
            this.homeScore = homeScore;
            this.awayTeam = awayTeam;
            this.awayScore = awayScore;
        }

        @Override
        public String toString() {
            return String.format("%s %d - %s %d",
                    homeTeam, homeScore, awayTeam, awayScore);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GameInfo other)) return false;
            return homeScore == other.homeScore &&
                    awayScore == other.awayScore &&
                    homeTeam.equals(other.homeTeam) &&
                    awayTeam.equals(other.awayTeam);
        }

        @Override
        public int hashCode() {
            return Objects.hash(homeTeam, homeScore, awayTeam, awayScore);
        }
    }

    /** Helper to build the map key – order matters. */
    private static String makeKey(String home, String away) {
        return home + "|" + away;
    }
}
