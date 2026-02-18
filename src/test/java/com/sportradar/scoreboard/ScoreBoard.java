package com.sportradar.scoreboard;

import java.util.List;

public class ScoreBoard {
    public void startGame(String mexico, String canada) {
    }

    public List<GameInfo> getSummary() {
    }

    public void finishGame(String germany, String france) {
    }

    public void updateScore(String uruguay, String italy, int i, int i1) {
    }

    public static class GameInfo {
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
    }
}
