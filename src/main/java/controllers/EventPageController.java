package controllers;

import com.jfoenix.controls.*;
import helpers.matchTable.MatchTableView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Gender;
import models.Modality;
import javafx.stage.Stage;
import models.Event;
import models.Team;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventPageController {
    public Label lblEventName;
    public JFXComboBox cbxModalities;
    public HBox paneGenders;
    public AnchorPane paneManager;
    public AnchorPane paneTeamGrid;
    public AnchorPane paneGroupTable;
    public Label lblModalityAndGender1, lblModalityAndGender2;
    public JFXTreeTableView matchTableView;
    public TreeTableColumn<MatchTableView, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableView, Number> positionMatchColumn, scoreboardAColumn, scoreboardBColumn;
    private TreeItem<MatchTableView> rootMatch = new TreeItem<>(new MatchTableView());
    private ToggleGroup genderGroup;

    static Event event;
    static Gender actualGender;
    static String modalityAndGender;

    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        event = findEventById();
        lblEventName.setText(event.getName());

        appendModalitiesInComboBox();
        cbxModalities.setValue(cbxModalities.getItems().get(0));
        generateGenderToggles();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        actualGender = getSelectedGender();
        changeModalityAndGender();

        generateColumns();
        matchTableView.setRoot(rootMatch);

        loadTeamGridView();
        loadGroupTableView();
    }

    private void generateColumns(){
        positionMatchColumn.setCellValueFactory(param -> param.getValue().getValue().positionProperty());
        versusColumn.setCellValueFactory(param -> param.getValue().getValue().versusProperty());
        teamAColumn.setCellValueFactory(param -> param.getValue().getValue().teamAProperty());
        teamBColumn.setCellValueFactory(param -> param.getValue().getValue().teamBProperty());
        scoreboardAColumn.setCellValueFactory(param -> param.getValue().getValue().scoreAProperty());
        scoreboardBColumn.setCellValueFactory(param -> param.getValue().getValue().scoreBProperty());
    }

    private void generateGenderToggles(){
        getSelectedModality().getGenders().forEach(gender -> {
            JFXRadioButton rdbGender = new JFXRadioButton(gender.getName());
            rdbGender.setOnAction(e -> changeModalityAndGender());
            rdbGender.setToggleGroup(genderGroup);
            paneGenders.getChildren().add(rdbGender);
        });
    }

    private Modality getSelectedModality(){
        return event.getModalities().stream().filter(modality ->
                modality.getName().equals(cbxModalities.getValue().toString().toLowerCase())).findAny().orElse(null);
    }

    private Gender getSelectedGender(){
        return getSelectedModality().getGenders().stream().filter(gender ->
                gender.getName().equals(((JFXRadioButton) genderGroup.getSelectedToggle()).getText()))
                .findAny().orElse(null);
    }

    @FXML
    protected void changeModality(){
        genderGroup.getToggles().clear();
        paneGenders.getChildren().clear();
        generateGenderToggles();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        changeModalityAndGender();
    }

    @FXML
    protected void changeModalityAndGender(){
        lblModalityAndGender1.setText(getModalityAndGender().toLowerCase());
        lblModalityAndGender2.setText(getModalityAndGender().toLowerCase());
        modalityAndGender = getModalityAndGender();
    }

    @FXML
    protected void openMatchFormView(){
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

    private Event findEventById(){
        return EventListController.institutionEvents.stream().filter(
                event -> event.getId().equals(EventItemController.eventId)).findFirst().orElse(null);
    }

    private void appendModalitiesInComboBox(){
        List<String> modalities = new ArrayList<>();
        event.getModalities().forEach(modality -> modalities.add(modality.getName()));

        cbxModalities.setItems(FXCollections.observableList(modalities));
    }

    private String getModalityAndGender(){
        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();
        return cbxModalities.getValue().toString() + " " + rdb.getText();
    }

    private void loadTeamGridView(){
        try{
            URL viewURL = getClass().getResource("/views/home/team-grid.fxml");
            paneTeamGrid.getChildren().add(FXMLLoader.load(viewURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGroupTableView(){
        GroupTableController.groups = actualGender.getTeams().isEmpty() ?
                new ArrayList<>() : getGenderGroups();

        try{
            URL viewURL = getClass().getResource("/views/home/group-table.fxml");
            paneGroupTable.getChildren().add(FXMLLoader.load(viewURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<List<Team>> getGenderGroups(){
        int lastGroupIndex = actualGender.getTeams().stream()
                .max(Comparator.comparing(Team::getGroup))
                .get().getGroup();
        List<List<Team>> groups = IntStream.range(0, lastGroupIndex+1)
                .<List<Team>>mapToObj(x -> new ArrayList<>()).collect(Collectors.toList());

        actualGender.getTeams().forEach(team -> groups.get(team.getGroup()).add(team));
        return groups;
    }
}
