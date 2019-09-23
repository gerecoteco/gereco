package helpers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LeaderBoardView {
    private SimpleIntegerProperty position;
    private SimpleStringProperty name;
    private SimpleIntegerProperty points;
    private SimpleIntegerProperty ownPoints;
    private SimpleIntegerProperty againstPoints;
    private SimpleIntegerProperty balance;
    private SimpleIntegerProperty fouls;

    public LeaderBoardView(){}

    public LeaderBoardView(int position, String name, int points, int ownPoints,
                           int againstPoints, int balance, int fouls) {
        this.position = new SimpleIntegerProperty(position);
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleIntegerProperty(points);
        this.ownPoints = new SimpleIntegerProperty(ownPoints);
        this.againstPoints = new SimpleIntegerProperty(againstPoints);
        this.balance = new SimpleIntegerProperty(balance);
        this.fouls = new SimpleIntegerProperty(fouls);
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public SimpleIntegerProperty pointsProperty() {
        return points;
    }
    public SimpleIntegerProperty ownPointsProperty() {
        return ownPoints;
    }
    public SimpleIntegerProperty againstPointsProperty() {
        return againstPoints;
    }
    public SimpleIntegerProperty balanceProperty() {
        return balance;
    }
    public SimpleIntegerProperty foulsProperty() {
        return fouls;
    }
}
