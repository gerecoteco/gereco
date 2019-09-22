package models;

import java.util.ArrayList;
import java.util.List;

public class Match {
    private List<String> teams;
    private List<Score> scores;

    public Match(List<String> teams) {
        this.teams = teams;
        this.scores = new ArrayList<>();
    }

    public List<String> getTeams() {
        return teams;
    }
    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
    public List<Score> getScores() {
        return scores;
    }
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
