package models;

public class Team {
    private String name;
    private Score score;

    public Team(String name, Score score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Score getScore() {
        return score;
    }
    public void setScore(Score score) {
        this.score = score;
    }
}
