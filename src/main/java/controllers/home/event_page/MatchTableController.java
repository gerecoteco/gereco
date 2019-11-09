package controllers.home.event_page;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import com.jfoenix.controls.JFXTreeTableView;
import controllers.home.event_list.EventItemController;
import controllers.home.HomeController;
import helpers.MatchTableView;
import helpers.MatchesGenerator;
import helpers.UTF8Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Match;
import models.Score;
import models.Team;
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static controllers.home.event_page.EventPageController.*;

public class MatchTableController implements Initializable {
    public JFXTreeTableView matchTableView;
    public HBox hboxButtons;
    public JFXButton btnFinalMatches;
    public TreeTableColumn<MatchTableView, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableView, Number> stageColumn, scoreboardAColumn, scoreboardBColumn;
    public Label lblModalityAndGender;
    private TreeItem<MatchTableView> rootMatch = new TreeItem<>(new MatchTableView());

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private int rowIndex = 0;
    private TreeItem<MatchTableView> draggedItem;
    private ObservableList<TreeItem<MatchTableView>> nextItems;

    public static TreeItem<MatchTableView> selectedMatch;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        lblModalityAndGender.setText(modalityAndGender);

        matchTableView.setRoot(rootMatch);
        generateColumns();
        if(!actualGender.getMatches().isEmpty()) listMatchesOnTable();
        if(isFinalRound() || actualGender.getMatches().isEmpty()) hboxButtons.getChildren().remove(btnFinalMatches);

        setRowFactoryOfTable();
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

        HomeController.loadEventPageView();
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

    private void setRowFactoryOfTable(){
        matchTableView.setRowFactory(tv -> {
            TreeTableRow<MatchTableView> row = new TreeTableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();
                    draggedItem = rootMatch.getChildren().get(index);

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    rootMatch.getChildren().remove(draggedItem);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                matchTableView.getSelectionModel().clearSelection();

                rootMatch.getChildren().removeIf(item -> item.getValue().stageProperty() == null);

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    nextItems = FXCollections.observableArrayList();

                    for (int x = row.getIndex(); x < rootMatch.getChildren().size(); x++)
                        nextItems.add(rootMatch.getChildren().get(x));

                    resetMatchTableItens();

                    rowIndex = row.getIndex();
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int dropIndex = row.isEmpty() ? rootMatch.getChildren().size() : row.getIndex();

                    if(dropIndex != rootMatch.getChildren().size()){
                        rootMatch.getChildren().get(rowIndex).setValue(draggedItem.getValue());
                        matchTableView.getSelectionModel().select(dropIndex);
                    } else {
                        rootMatch.getChildren().add(draggedItem);
                        matchTableView.getSelectionModel().select(rootMatch.getChildren().size()-1);
                    }

                    event.setDropCompleted(true);
                    event.consume();
                }
            });
            return row ;
        });
    }

    private void resetMatchTableItens(){
        rootMatch.getChildren().removeAll(nextItems);
        rootMatch.getChildren().add(new TreeItem<>(new MatchTableView()));
        rootMatch.getChildren().addAll(nextItems);
    }
}
