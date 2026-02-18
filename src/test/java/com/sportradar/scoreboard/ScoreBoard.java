package com.sportradar.scoreboard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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



    public void startGame(String mexico, String canada) {
    }

    public List<GameInfo> getSummary() {
    }

    public void finishGame(String germany, String france) {
    }

    public void updateScore(String uruguay, String italy, int i, int i1) {
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
            if (!(o instanceof GameInfo)) return false;
            GameInfo other = (GameInfo) o;
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
}
