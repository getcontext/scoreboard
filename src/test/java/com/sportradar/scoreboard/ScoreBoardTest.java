package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreBoardTest {

    private ScoreBoard board;

    @BeforeEach
    void setUp() {
        board = new ScoreBoard();
    }

    @Test
    void startGameAddsGameWithZeroZeroScore() {
        board.startGame("Mexico", "Canada");
        List<ScoreBoard.GameInfo> summary = board.getSummary();
        assertEquals(1, summary.size());
        ScoreBoard.GameInfo g = summary.get(0);
        assertEquals("Mexico", g.homeTeam);
        assertEquals(0, g.homeScore);
        assertEquals("Canada", g.awayTeam);
        assertEquals(0, g.awayScore);
    }

    @Test
    void startGameDuplicateThrows() {
        board.startGame("Spain", "Brazil");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> board.startGame("Spain", "Brazil"));
        assertTrue(ex.getMessage().contains("already started"));
    }

    @Test
    void finishGameRemovesIt() {
        board.startGame("Germany", "France");
        board.finishGame("Germany", "France");
        assertTrue(board.getSummary().isEmpty());
    }

    @Test
    void finishNonexistentGameThrows() {
        assertThrows(NoSuchElementException.class,
                () -> board.finishGame("Argentina", "Australia"));
    }

    @Test
    void updateScoreChangesValues() {
        board.startGame("Uruguay", "Italy");
        board.updateScore("Uruguay", "Italy", 6, 6);
        List<ScoreBoard.GameInfo> summary = board.getSummary();
        ScoreBoard.GameInfo g = summary.get(0);
        assertEquals(6, g.homeScore);
        assertEquals(6, g.awayScore);
    }

    @Test
    void updateScoreNegativeThrows() {
        board.startGame("Argentina", "Australia");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> board.updateScore("Argentina", "Australia", -1, 2));
        assertTrue(ex.getMessage().contains("non‑negative"));
    }

    @Test
    void updateScoreNonexistentThrows() {
        assertThrows(NoSuchElementException.class,
                () -> board.updateScore("Non", "Existing", 1, 2));
    }

    @Test
    void summaryIsOrderedByTotalScoreAndRecency() {
        // Insert in a particular order to test tie‑breaker
        board.startGame("Mexico", "Canada");
        board.updateScore("Mexico", "Canada", 0, 5);          // total 5

        board.startGame("Spain", "Brazil");
        board.updateScore("Spain", "Brazil", 10, 2);        // total 12

        board.startGame("Germany", "France");
        board.updateScore("Germany", "France", 2, 2);        // total 4

        board.startGame("Uruguay", "Italy");
        board.updateScore("Uruguay", "Italy", 6, 6);       // total 12 (added after Spain/Brazil)

        board.startGame("Argentina", "Australia");
        board.updateScore("Argentina", "Australia", 3, 1); // total 4 (added after Germany/France)

        List<ScoreBoard.GameInfo> summary = board.getSummary();

        assertEquals(5, summary.size());
        // Expected order (most recent wins on equal totals)
        ScoreBoard.GameInfo[] expected = {
                new ScoreBoard.GameInfo("Uruguay", 6, "Italy", 6),
                new ScoreBoard.GameInfo("Spain", 10, "Brazil", 2),
                new ScoreBoard.GameInfo("Mexico", 0, "Canada", 5),
                new ScoreBoard.GameInfo("Argentina", 3, "Australia", 1),
                new ScoreBoard.GameInfo("Germany", 2, "France", 2)
        };

        assertArrayEquals(expected, summary.toArray());
    }
}
