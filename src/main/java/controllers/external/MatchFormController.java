package controllers.external;

import controllers.home.event_list.EventItemController;
import controllers.home.HomeController;
import controllers.home.event_page.MatchTableController;
import helpers.MatchTableModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import models.Match;
import models.Score;
import services.EventService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static controllers.home.event_page.EventPageController.actualGender;
import static controllers.home.event_page.EventPageController.event;

public class MatchFormController implements Initializable {
    public Label lblTeamA;
    public Label lblTeamB;
    public TextField txtOwnPointsA;
    public TextField txtOwnPointsB;
    public TextField txtFoulsA;
    public TextField txtFoulsB;

    private TreeItem<MatchTableModel> selectedMatch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedMatch = MatchTableController.selectedMatch;
        Match actualMatch = getActualMatch();

        lblTeamA.setText(actualMatch.getTeams().get(0));
        lblTeamB.setText(actualMatch.getTeams().get(1));
        txtOwnPointsA.setText(String.valueOf(actualMatch.getScores().get(0).getOwnPoints()));
        txtOwnPointsB.setText(String.valueOf(actualMatch.getScores().get(1).getOwnPoints()));
        txtFoulsA.setText(String.valueOf(actualMatch.getScores().get(0).getFouls()));
        txtFoulsB.setText(String.valueOf(actualMatch.getScores().get(1).getFouls()));

        makeTextFieldNumericOnly(txtOwnPointsA);
        makeTextFieldNumericOnly(txtOwnPointsB);
        makeTextFieldNumericOnly(txtFoulsA);
        makeTextFieldNumericOnly(txtFoulsB);
    }

    private void makeTextFieldNumericOnly(TextField txt){
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                txt.setText(newValue.replaceAll("[^\\d]", ""));
            if (newValue.isEmpty()) txt.setText("0");
        });
    }

    @FXML
    protected void saveMatch(){
        Match actualMatch = getActualMatch();
        int teamAOwnPoints = Integer.parseInt(txtOwnPointsA.getText());
        int teamBOwnPoints = Integer.parseInt(txtOwnPointsB.getText());

        int teamAPoints, teamBPoints;
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
        ((Stage) lblTeamA.getScene().getWindow()).close();
        HomeController.loadEventPageView();
    }

    private void addMatchScoreOnTeam(){
        actualGender.getTeams().forEach(team -> {
            team.setScore(new Score());
            findTeamMatches(team.getName()).forEach(match -> {
                Score matchScore = match.getScores().get(match.getTeams().indexOf(team.getName()));

                team.getScore().setPoints(team.getScore().getPoints() + matchScore.getPoints());
                team.getScore().setOwnPoints(team.getScore().getOwnPoints() + matchScore.getOwnPoints());
                team.getScore().setAgainstPoints(team.getScore().getAgainstPoints() + matchScore.getAgainstPoints());
                team.getScore().setBalance(team.getScore().getBalance() +
                        (matchScore.getOwnPoints() - matchScore.getAgainstPoints()));
                team.getScore().setFouls(team.getScore().getFouls() + matchScore.getFouls());
            });
        });
    }

    private List<Match> findTeamMatches(String teamName){
        return actualGender.getMatches().stream().filter(match ->
                match.getTeams().contains(teamName)).collect(Collectors.toList());
    }

    private Match getActualMatch(){
        return actualGender.getMatches().stream().filter(match ->
                match.getTeams().equals(Arrays.asList(selectedMatch.getValue().teamAProperty().get(),
                        selectedMatch.getValue().teamBProperty().get()))).findAny().orElse(null);
    }
}
