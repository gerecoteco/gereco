package helpers.groupTable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

public class GroupTableView extends TreeItem<GroupTableView> {
    private SimpleStringProperty team;
    private SimpleIntegerProperty points;
    private SimpleIntegerProperty ownPoints;
    private SimpleIntegerProperty againstPoints;
    private SimpleIntegerProperty balance;
    private SimpleIntegerProperty fouls;

    public GroupTableView() {
    }

    public GroupTableView(String team, int points, int ownPoints, int againstPoints, int balance, int fouls) {
        this.team = new SimpleStringProperty(team);
        this.points = new SimpleIntegerProperty(points);
        this.ownPoints = new SimpleIntegerProperty(ownPoints);
        this.againstPoints = new SimpleIntegerProperty(againstPoints);
        this.balance = new SimpleIntegerProperty(balance);
        this.fouls = new SimpleIntegerProperty(fouls);
    }

    public SimpleStringProperty teamProperty() {
        return team;
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
