package helpers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MatchTableModel {
    private SimpleStringProperty modality;
    private SimpleStringProperty gender;
    private SimpleIntegerProperty stage;
    private SimpleStringProperty versus;
    private SimpleStringProperty teamA;
    private SimpleStringProperty teamB;
    private SimpleIntegerProperty scoreA;
    private SimpleIntegerProperty scoreB;

    public MatchTableModel() {
    }

    public MatchTableModel(String modality, String gender, int stage,
                           String teamA, String teamB) {
        this.modality = new SimpleStringProperty(modality);
        this.gender = new SimpleStringProperty(gender);
        this.stage = new SimpleIntegerProperty(stage);
        this.versus = new SimpleStringProperty("X");
        this.teamA = new SimpleStringProperty(teamA);
        this.teamB =  new SimpleStringProperty(teamB);
    }

    public MatchTableModel(int stage, String teamA, String teamB, int scoreA, int scoreB) {
        this.stage = new SimpleIntegerProperty(stage);
        this.versus = new SimpleStringProperty("X");
        this.teamA = new SimpleStringProperty(teamA);
        this.teamB = new SimpleStringProperty(teamB);
        this.scoreA = new SimpleIntegerProperty(scoreA);
        this.scoreB = new SimpleIntegerProperty(scoreB);
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
    public SimpleIntegerProperty stageProperty() {
        return stage;
    }
    public SimpleStringProperty modalityProperty() {
        return modality;
    }
    public SimpleStringProperty genderProperty() {
        return gender;
    }
}
