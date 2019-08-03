package models;

public class Team {
    private String name;
    private String tag;
    private Score score;

    public Team(String name, String tag) {
        this.name = name;
        this.tag = tag;
        this.score = new Score();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public Score getScore() {
        return score;
    }
    public void setScore(Score score) {
        this.score = score;
    }
}
