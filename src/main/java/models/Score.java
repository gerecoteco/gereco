package models;

public class Score {
    private int points;
    private int balance;
    private int ownPoints;
    private int againstPoints;
    private int fouls;

    public Score(int points, int balance, int ownPoints, int againstPoints, int fouls) {
        this.points = points;
        this.balance = balance;
        this.ownPoints = ownPoints;
        this.againstPoints = againstPoints;
        this.fouls = fouls;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
    public int getBalance() {
        return balance;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public int getOwnPoints() {
        return ownPoints;
    }
    public void setOwnPoints(int ownPoints) {
        this.ownPoints = ownPoints;
    }
    public int getAgainstPoints() {
        return againstPoints;
    }
    public void setAgainstPoints(int againstPoints) {
        this.againstPoints = againstPoints;
    }
    public int getFouls() {
        return fouls;
    }
    public void setFouls(int fouls) {
        this.fouls = fouls;
    }
}
