package models;

public class Match {
    private String[] teamsId;
    private Score[] scores;

    public Match(String[] teamsId, Score[] scores) {
        this.teamsId = teamsId;
        this.scores = scores;
    }

    public String[] getTeamsId() {
        return teamsId;
    }
    public void setTeamsId(String[] teamsId) {
        this.teamsId = teamsId;
    }
    public Score[] getScores() {
        return scores;
    }
    public void setScores(Score[] scores) {
        this.scores = scores;
    }
}
