package controllers;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTreeTableView;
import helpers.MatchTableView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Match;
import models.Score;
import models.Team;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static controllers.EventPageController.actualGender;

public class MatchTableController {
    public JFXTreeTableView matchTableView;
    public TreeTableColumn<MatchTableView, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableView, Number> positionMatchColumn, scoreboardAColumn, scoreboardBColumn;
    public Label lblModalityAndGender2;
    private TreeItem<MatchTableView> rootMatch = new TreeItem<>(new MatchTableView());

    static TreeItem<MatchTableView> selectedMatch;

    @FXML
    public void initialize() {
        matchTableView.setRoot(rootMatch);
        generateColumns();

        if(!actualGender.getMatches().isEmpty()) listMatchesOnTable();
    }

    private void listMatchesOnTable(){
        rootMatch.getChildren().clear();
        List<Match> matches = actualGender.getMatches();

        for(int x=0; x < matches.size(); x++){
            List<String> teamsName = matches.get(x).getTeams();
            List<Score> scores = matches.get(x).getScores();

            TreeItem<MatchTableView> matchRow = new TreeItem<>(new MatchTableView(x+1, teamsName.get(0),
                    teamsName.get(1), scores.get(0).getOwnPoints(), scores.get(1).getOwnPoints()));
            rootMatch.getChildren().add(matchRow);
        }
    }

    @FXML
    protected void openMatchFormView(){
        selectedMatch = (TreeItem<MatchTableView>) matchTableView.getSelectionModel().getSelectedItem();

        if(!matchTableView.getSelectionModel().isEmpty()){
            try {
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource(
                        "/views/external-forms/match-form.fxml")));
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.centerOnScreen();
                stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            showToastMessage("Selecione uma partida primeiramente");
    }

    @FXML
    protected void generateFinalMatches(){
        actualGender.getTeams().sort(Comparator.comparing((Team team) -> team.getScore().getPoints())
                .thenComparing((Team team) -> team.getScore().getOwnPoints())
                .thenComparing((Team team) -> team.getScore().getBalance())
                .thenComparing((Team team) -> team.getScore().getFouls()).reversed());
    }

    private void generateColumns(){
        positionMatchColumn.setCellValueFactory(param -> param.getValue().getValue().positionProperty());
        versusColumn.setCellValueFactory(param -> param.getValue().getValue().versusProperty());
        teamAColumn.setCellValueFactory(param -> param.getValue().getValue().teamAProperty());
        teamBColumn.setCellValueFactory(param -> param.getValue().getValue().teamBProperty());
        scoreboardAColumn.setCellValueFactory(param -> param.getValue().getValue().scoreAProperty());
        scoreboardBColumn.setCellValueFactory(param -> param.getValue().getValue().scoreBProperty());
    }

    private void showToastMessage(String messsage) {
        JFXSnackbar snackbar = new JFXSnackbar(HomeController.staticStackPaneMain);
        snackbar.getStylesheets().add(getClass().getResource("/css/snackbar.css").toString());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(messsage, "OK", action -> snackbar.close()),
                Duration.INDEFINITE, null));
    }
}
