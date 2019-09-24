package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTreeTableView;
import helpers.MatchTableView;
import helpers.MatchesGenerator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Match;
import models.Score;
import models.Team;
import services.EventService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static controllers.EventPageController.*;

public class MatchTableController {
    public JFXTreeTableView matchTableView;
    public HBox hboxButtons;
    public JFXButton btnFinalMatches;
    public TreeTableColumn<MatchTableView, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableView, Number> stageColumn, scoreboardAColumn, scoreboardBColumn;
    public Label lblModalityAndGender;
    private TreeItem<MatchTableView> rootMatch = new TreeItem<>(new MatchTableView());

    static TreeItem<MatchTableView> selectedMatch;

    @FXML
    public void initialize() {
        lblModalityAndGender.setText(modalityAndGender);

        matchTableView.setRoot(rootMatch);
        generateColumns();
        if(!actualGender.getMatches().isEmpty()) listMatchesOnTable();
        if(isFinalRound()) hboxButtons.getChildren().remove(btnFinalMatches);
    }

    private boolean isFinalRound(){
        return actualGender.getMatches().stream().filter(
                match -> match.getStage() == 2).findAny().orElse(null) != null;
    }

    private void listMatchesOnTable(){
        rootMatch.getChildren().clear();
        List<Match> matches = actualGender.getMatches();

        matches.forEach(match -> {
            List<String> teamsName = match.getTeams();
            List<Score> scores = match.getScores();
            TreeItem<MatchTableView> matchRow = new TreeItem<>(new MatchTableView(
                    match.getStage(), teamsName.get(0), teamsName.get(1),
                    scores.get(0).getOwnPoints(), scores.get(1).getOwnPoints()));

            rootMatch.getChildren().add(matchRow);
        });
    }

    @FXML
    protected void openMatchFormView(){
        selectedMatch = (TreeItem<MatchTableView>) matchTableView.getSelectionModel().getSelectedItem();

        if(!matchTableView.getSelectionModel().isEmpty()){
            if(isFinalRound() && selectedMatch.getValue().stageProperty().get() == 1)
                showToastMessage("Você não pode alterar partidas da primeira etapa");
            else
                loadMatchForm();
        } else
            showToastMessage("Selecione uma partida primeiramente");
    }

    private void loadMatchForm(){
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
    }

    @FXML
    protected void generateFinalMatches(){
        List<Team> finalGroup = new ArrayList<>();

        getGenderGroups().forEach(teams -> {
            teams.sort(Comparator.comparing((Team team) -> team.getScore().getPoints())
                    .thenComparing((Team team) -> team.getScore().getOwnPoints())
                    .thenComparing((Team team) -> team.getScore().getBalance())
                    .thenComparing((Team team) -> team.getScore().getFouls()).reversed());

            finalGroup.add(teams.get(0));
        });

        List<Match> matches = new MatchesGenerator().generateGroupMatches(finalGroup);
        matches.forEach(match -> match.setStage(2));

        actualGender.getMatches().addAll(matches);
        new EventService().updateEvent(EventItemController.eventId, event);

        HomeController.loadView(getClass().getResource("/views/home/event-page.fxml"));
    }

    private void generateColumns(){
        stageColumn.setCellValueFactory(param -> param.getValue().getValue().stageProperty());
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
