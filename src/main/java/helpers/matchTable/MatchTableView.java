package helpers.matchTable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MatchTableView {
    private SimpleIntegerProperty position;
    private SimpleStringProperty versus;
    private SimpleStringProperty teamA;
    private SimpleStringProperty teamB;
    private SimpleIntegerProperty scoreA;
    private SimpleIntegerProperty scoreB;

    public MatchTableView() {
    }

    public MatchTableView(int position, String teamA, String teamB, int scoreA, int scoreB) {
        this.position = new SimpleIntegerProperty(position);
        this.versus = new SimpleStringProperty("X");
        this.teamA = new SimpleStringProperty(teamA);
        this.teamB = new SimpleStringProperty(teamB);
        this.scoreA = new SimpleIntegerProperty(scoreA);
        this.scoreB = new SimpleIntegerProperty(scoreB);
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    public SimpleStringProperty versusProperty() {
        return versus;
    }

    public SimpleStringProperty teamAProperty() {
        return teamA;
    }

    public SimpleStringProperty teamBProperty() {
        return teamB;
    }

    public SimpleIntegerProperty scoreAProperty() {
        return scoreA;
    }

    public SimpleIntegerProperty scoreBProperty() {
        return scoreB;
    }
}
