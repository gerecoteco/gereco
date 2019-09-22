package controllers;

import com.jfoenix.controls.*;
import helpers.DialogBuilder;
import helpers.TeamGroupsManager;
import helpers.groupTable.GroupTableView;
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
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class EventPageController {
    public Label lblEventName;
    public JFXComboBox cbxModalities;
    public ToggleGroup genderGroup;
    public HBox paneGenders;
    public AnchorPane paneManager;
    public AnchorPane paneTeamGrid;
    public JFXComboBox cbxGroups;
    public Label lblModalityAndGender1, lblModalityAndGender2;
    public JFXTreeTableView matchTableView;
    public TreeTableColumn<MatchTableView, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableView, Number> positionMatchColumn, scoreboardAColumn, scoreboardBColumn;
    public JFXTreeTableView groupTableView;
    public TreeTableColumn<GroupTableView, String> teamCollumn;
    public TreeTableColumn<GroupTableView, Number> ownPointsColumn,
            againstPointsColumn, balanceColumn, foulsColumn;

    private TreeItem<MatchTableView> rootMatch = new TreeItem<>(new MatchTableView());
    private TreeItem<GroupTableView> rootGroup = new TreeItem<>(new GroupTableView());

    private Event event;
    private List<List<Team>> groups;

    @FXML
    public void initialize() {
        genderGroup = new ToggleGroup();
        event = findEventById();
        lblEventName.setText(event.getName());

        appendModalitiesInComboBox();
        cbxModalities.setValue(cbxModalities.getItems().get(0));
        generateGenderToggles();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        changeModalityAndGender();

        generateColumns();
        matchTableView.setRoot(rootMatch);
        groupTableView.setRoot(rootGroup);
        loadTeamGridView();
    }

    private void generateColumns(){
        positionMatchColumn.setCellValueFactory(param -> param.getValue().getValue().positionProperty());
        versusColumn.setCellValueFactory(param -> param.getValue().getValue().versusProperty());
        teamAColumn.setCellValueFactory(param -> param.getValue().getValue().teamAProperty());
        teamBColumn.setCellValueFactory(param -> param.getValue().getValue().teamBProperty());
        scoreboardAColumn.setCellValueFactory(param -> param.getValue().getValue().scoreAProperty());
        scoreboardBColumn.setCellValueFactory(param -> param.getValue().getValue().scoreBProperty());

        teamCollumn.setCellValueFactory(param -> param.getValue().getValue().teamProperty());
        ownPointsColumn.setCellValueFactory(param -> param.getValue().getValue().ownPointsProperty());
        againstPointsColumn.setCellValueFactory(param -> param.getValue().getValue().againstPointsProperty());
        balanceColumn.setCellValueFactory(param -> param.getValue().getValue().balanceProperty());
        foulsColumn.setCellValueFactory(param -> param.getValue().getValue().foulsProperty());
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

    @FXML
    private void loadGroupDialog(){
        String heading = "Agrupar times";
        String body = "Tem certeza que deseja agrupar os times de " + getModalityAndGender() + "?" +
                "\n(você não poderá mais alterar os times de " + getModalityAndGender() + ")";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, HomeController.staticStackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            generateGroups();
            dialog.close();
        });
        dialog.show();
    }

    private void generateGroups(){
        TeamGroupsManager teamGroupsManager = new TeamGroupsManager();

        teamGroupsManager.groupAllTeamsByTag(TeamsGridController.teams, getTeamTags());
        groups = teamGroupsManager.generateGroupsAndReturn(3);
        getSelectedGender().setTeams(teamGroupsManager.getOrderedTeams());

        new EventService().updateEvent(EventItemController.eventId, event);
        generateCbxGroupsItens();
        cbxGroups.setValue(1);
    }

    @FXML
    protected void showGroupsOnTable(){
        int groupIndex = (int) cbxGroups.getValue() - 1;
        rootGroup.getChildren().clear();

        groups.get(groupIndex).forEach(team -> {
            TreeItem<GroupTableView> teamRow = new TreeItem<>(new GroupTableView(team.getName(),
                    team.getScore().getOwnPoints(), team.getScore().getAgainstPoints(),
                    team.getScore().getBalance(), team.getScore().getFouls()));
            rootGroup.getChildren().add(teamRow);
        });
    }

    private void generateCbxGroupsItens(){
        IntStream.range(0, groups.size()).forEachOrdered(x -> cbxGroups.getItems().add(x + 1));
    }

    private List<String> getTeamTags(){
        List<String> tags = new ArrayList<>();
        TeamsGridController.teams.forEach(team -> {
            if(!tags.contains(team.getTag())) tags.add(team.getTag());
        });
        return tags;
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
}
