package controllers.home.event_page;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.*;
import controllers.home.HomeController;
import controllers.home.event_list.EventItemController;
import helpers.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GeneralMatch;
import models.Match;
import models.Score;
import models.Team;
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.home.event_page.EventPageController.*;

public class MatchTableController implements Initializable {
    public JFXTreeTableView matchTableView;
    public HBox hboxButtons;
    public JFXButton btnFinalMatches;
    public TreeTableColumn<MatchTableModel, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableModel, Number> stageColumn, scoreboardAColumn, scoreboardBColumn;
    public Label lblModalityAndGender;
    private TreeItem<MatchTableModel> rootMatch = new TreeItem<>(new MatchTableModel());

    public static TreeItem<MatchTableModel> selectedMatch;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        lblModalityAndGender.setText(modalityAndGender);

        matchTableView.setRoot(rootMatch);
        generateColumns();
        if(!actualGender.getMatches().isEmpty()) listMatchesOnTable();
        if(isFinalRound() || actualGender.getMatches().isEmpty()) hboxButtons.getChildren().remove(btnFinalMatches);
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
            TreeItem<MatchTableModel> matchRow = new TreeItem<>(new MatchTableModel(
                    match.getStage(), teamsName.get(0), teamsName.get(1),
                    scores.get(0).getOwnPoints(), scores.get(1).getOwnPoints()));

            rootMatch.getChildren().add(matchRow);
        });
    }

    @FXML
    protected void downloadMatchTablePDF() throws IOException, DocumentException {
        PdfTableGenerator pdfTableGenerator = new PdfTableGenerator();
        String title = strings.getString("matchTable") + modalityAndGender;
        String fileName = title + " - " + LocalDate.now() + ".pdf";

        pdfTableGenerator.generateMatchTablePdf(title, "../../../" + fileName, rootMatch.getChildren());
        HomeController.showToastMessage(strings.getString("successDownloadPDF"));
    }

    @FXML
    protected void openMatchFormView(){
        selectedMatch = (TreeItem<MatchTableModel>) matchTableView.getSelectionModel().getSelectedItem();

        if(!matchTableView.getSelectionModel().isEmpty()){
            if(isFinalRound() && selectedMatch.getValue().stageProperty().get() == 1)
                showToastMessage(strings.getString("cantChangeMatches"));
            else
                loadMatchForm();
        } else
            showToastMessage(strings.getString("selectMatchFirst"));
    }

    private void loadMatchForm(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/views/external/match-form.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            Scene scene = new Scene(root);

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
    protected void loadGenerateFinalMatchesDialog(){
        String heading = strings.getString("generateFinalMatchDialog.heading");
        String body = strings.getString("generateFinalMatchDialog.body");
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, HomeController.staticStackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            generateFinalMatches();
            HomeController.showToastMessage(strings.getString("successGenerateFinalMatches"));
            dialog.close();
        });

        dialog.show();
    }

    private void generateFinalMatches(){
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
        addFinalMatchesToEvent(matches);
        new EventService().updateEvent(EventItemController.eventId, event);

        HomeController.loadEventPageView();
    }

    private void addFinalMatchesToEvent(List<Match> finalMatches){
        for (Match match : finalMatches)
            event.getMatches().get(0).add(new GeneralMatch(actualModality.getName(),
                    actualGender.getName(), match.getStage(), match.getTeams().get(0), match.getTeams().get(1)));
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
        snackbar.getStylesheets().add(getClass().getResource("/css/external/snackbar.css").toString());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(messsage, "OK", action -> snackbar.close()),
                Duration.INDEFINITE, null));
    }
}
