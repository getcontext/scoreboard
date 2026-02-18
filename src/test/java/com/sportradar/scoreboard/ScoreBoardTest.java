package com.sportradar.scoreboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}
