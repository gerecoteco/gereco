package models;

public class Match {
    private String[] teams;
    private Score[] scores;

    public Match(String[] teams, Score[] scores) {
        this.teams = teams;
        this.scores = scores;
    }

    public String[] getTeams() {
        return teams;
    }
    public void setTeams(String[] teams) {
        this.teams = teams;
    }
    public Score[] getScores() {
        return scores;
    }
    public void setScores(Score[] scores) {
        this.scores = scores;
    }
}
