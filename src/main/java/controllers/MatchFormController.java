package controllers;

import helpers.matchTable.MatchTableView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import models.Match;
import models.Team;
import services.EventService;

import java.net.URL;
import java.util.Arrays;

import static controllers.EventPageController.actualGender;
import static controllers.EventPageController.event;

public class MatchFormController {
    public Label lblTeamA;
    public Label lblTeamB;
    public TextField txtOwnPointsA;
    public TextField txtOwnPointsB;
    public TextField txtFoulsA;
    public TextField txtFoulsB;

    private TreeItem<MatchTableView> selectedMatch;
    private int teamAPoints, teamBPoints;
    private int teamAOwnPoints, teamBOwnPoints;

    @FXML
    public void initialize() {
        selectedMatch = MatchTableController.selectedMatch;
        Match actualMatch = getActualMatch();

        lblTeamA.setText(actualMatch.getTeams().get(0));
        lblTeamB.setText(actualMatch.getTeams().get(1));
        txtOwnPointsA.setText(String.valueOf(actualMatch.getScores().get(0).getOwnPoints()));
        txtOwnPointsB.setText(String.valueOf(actualMatch.getScores().get(1).getOwnPoints()));
        txtFoulsA.setText(String.valueOf(actualMatch.getScores().get(0).getFouls()));
        txtFoulsB.setText(String.valueOf(actualMatch.getScores().get(1).getFouls()));
    }

    @FXML
    protected void saveMatch(){
        Match actualMatch = getActualMatch();
        teamAOwnPoints = Integer.parseInt(txtOwnPointsA.getText());
        teamBOwnPoints = Integer.parseInt(txtOwnPointsB.getText());

        if(teamAOwnPoints == teamBOwnPoints){
            teamAPoints = 1; teamBPoints = 1;
        } else if(teamAOwnPoints > teamBOwnPoints){
            teamAPoints = 3; teamBPoints = 0;
        } else {
            teamAPoints = 0; teamBPoints = 3;
        }

        actualMatch.getScores().get(0).setPoints(teamAPoints);
        actualMatch.getScores().get(1).setPoints(teamBPoints);

        actualMatch.getScores().get(0).setOwnPoints(teamAOwnPoints);
        actualMatch.getScores().get(1).setOwnPoints(teamBOwnPoints);

        actualMatch.getScores().get(0).setAgainstPoints(teamBOwnPoints);
        actualMatch.getScores().get(1).setAgainstPoints(teamAOwnPoints);

        actualMatch.getScores().get(0).setFouls(Integer.parseInt(txtFoulsA.getText()));
        actualMatch.getScores().get(1).setFouls(Integer.parseInt(txtFoulsB.getText()));

        actualMatch.getScores().get(0).setBalance(teamAOwnPoints - teamBOwnPoints);
        actualMatch.getScores().get(1).setBalance(teamBOwnPoints - teamAOwnPoints);

        addMatchScoreOnTeam();
        new EventService().updateEvent(EventItemController.eventId, event);
        reloadEventPage();
    }

    private void reloadEventPage(){
        URL eventPageURL = getClass().getResource("/views/home/event-page.fxml");
        HomeController.loadView(eventPageURL);
    }

    private void addMatchScoreOnTeam(){
        Team teamA = findTeamByName(lblTeamA.getText());
        Team teamB = findTeamByName(lblTeamB.getText());

        teamA.getScore().setPoints(teamAPoints);
        teamB.getScore().setPoints(teamBPoints);

        teamA.getScore().setOwnPoints(teamA.getScore().getOwnPoints() + teamAOwnPoints);
        teamB.getScore().setOwnPoints(teamB.getScore().getOwnPoints() + teamBOwnPoints);

        teamA.getScore().setAgainstPoints(teamA.getScore().getAgainstPoints() + teamBOwnPoints);
        teamB.getScore().setAgainstPoints(teamB.getScore().getAgainstPoints() + teamAOwnPoints);

        teamA.getScore().setBalance(teamA.getScore().getBalance() + (teamAOwnPoints - teamBOwnPoints));
        teamB.getScore().setBalance(teamB.getScore().getBalance() + (teamBOwnPoints - teamAOwnPoints));

        teamA.getScore().setFouls(teamA.getScore().getFouls() + Integer.parseInt(txtFoulsA.getText()));
        teamB.getScore().setFouls(teamB.getScore().getFouls() + Integer.parseInt(txtFoulsB.getText()));
    }

    private Team findTeamByName(String teamName){
        return actualGender.getTeams().stream().filter(team -> team.getName().equals(teamName)).findAny().get();
    }

    private Match getActualMatch(){
        return actualGender.getMatches().stream().filter(match ->
                match.getTeams().equals(Arrays.asList(selectedMatch.getValue().teamAProperty().get(),
                        selectedMatch.getValue().teamBProperty().get()))).findAny().orElse(null);
    }
}
