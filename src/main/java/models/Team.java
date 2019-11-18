package models;

public class Team {
    private String name;
    private String tag;
    private int group;
    private Score score;

    public Team(String name) {
        this.name = name;
        this.tag = "";
        this.group = 0;
        this.score = new Score();
    }

    public Team(String name, String tag) {
        this.name = name;
        this.tag = tag;
        this.group = 0;
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
    public int getGroup() {
        return group;
    }
    public void setGroup(int group) {
        this.group = group;
    }
}
