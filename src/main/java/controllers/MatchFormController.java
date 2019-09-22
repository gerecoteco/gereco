package controllers;

import helpers.matchTable.MatchTableView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import models.Match;

import java.util.Arrays;

import static controllers.EventPageController.actualGender;

public class MatchFormController {
    public Label lblTeamA;
    public Label lblTeamB;
    public TextField txtOwnPointsA;
    public TextField txtOwnPointsB;

    TreeItem<MatchTableView> selectedMatch;

    @FXML
    public void initialize() {
        selectedMatch = MatchTableController.selectedMatch;
        Match actualMatch = getActualMatch();

        lblTeamA.setText(actualMatch.getTeams().get(0));
        lblTeamB.setText(actualMatch.getTeams().get(1));
        txtOwnPointsA.setText(String.valueOf(actualMatch.getScores().get(0).getOwnPoints()));
        txtOwnPointsB.setText(String.valueOf(actualMatch.getScores().get(1).getOwnPoints()));

        System.out.println(getActualMatch());
    }

    private Match getActualMatch(){
        return actualGender.getMatches().stream().filter(match ->
                match.getTeams().equals(Arrays.asList(selectedMatch.getValue().teamAProperty().get(),
                        selectedMatch.getValue().teamBProperty().get()))).findAny().orElse(null);
    }
}
