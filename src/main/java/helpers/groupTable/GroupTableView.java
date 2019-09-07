package helpers.groupTable;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GroupTableView {
    private SimpleIntegerProperty position;
    private SimpleStringProperty team;
    private SimpleIntegerProperty ownPoints;
    private SimpleIntegerProperty againstPoints;
    private SimpleIntegerProperty balance;
    private SimpleIntegerProperty fouls;

    public GroupTableView() {
    }

    public GroupTableView(int position, String team, int ownPoints, int againstPoints, int balance, int fouls) {
        this.position = new SimpleIntegerProperty(position);
        this.team = new SimpleStringProperty(team);
        this.ownPoints = new SimpleIntegerProperty(ownPoints);
        this.againstPoints = new SimpleIntegerProperty(againstPoints);
        this.balance = new SimpleIntegerProperty(balance);
        this.fouls = new SimpleIntegerProperty(fouls);
    }

    public SimpleIntegerProperty positionProperty() {
        return position;
    }

    public SimpleStringProperty teamProperty() {
        return team;
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
