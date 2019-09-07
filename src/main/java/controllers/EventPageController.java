package controllers;

import com.jfoenix.controls.*;
import helpers.DialogBuilder;
import helpers.groupTable.GroupTableView;
import helpers.matchTable.MatchTableView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import models.Modality;
import javafx.stage.Stage;
import models.Event;
import models.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EventPageController {
    public Label lblEventName;
    public JFXComboBox cbxModalities;
    public GridPane gridTeams;
    public ToggleGroup teamGroup;
    public ToggleGroup genderGroup;
    public HBox paneGenders;
    public JFXTextField txtTeamName;
    public JFXTextField txtTeamTag;
    public AnchorPane paneManager;
    public Label lblModalityAndGender0, lblModalityAndGender1, lblModalityAndGender2;
    public JFXTreeTableView matchTableView;
    public TreeTableColumn<MatchTableView, String> versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableView, Number> positionMatchColumn, scoreboardAColumn, scoreboardBColumn;
    public JFXTreeTableView groupTableView;
    public TreeTableColumn<GroupTableView, String> teamCollumn;
    public TreeTableColumn<GroupTableView, Number> positionGroupColumn, ownPointsColumn, againstPointsColumn, balanceColumn, foulsColumn;

    private TreeItem<MatchTableView> rootMatch = new TreeItem<>(new MatchTableView());
    private TreeItem<GroupTableView> rootGroup = new TreeItem<>(new GroupTableView());

    private List<TreeItem<MatchTableView>> treeItemsMatch = new ArrayList<>();
    private List<TreeItem<GroupTableView>> treeItemsGroup = new ArrayList<>();

    private Event event;
    private List<Team> teams;

    @FXML
    public void initialize() {
        teams = new ArrayList<>();
        genderGroup = new ToggleGroup();
        event = findEventById();
        lblEventName.setText(event.getName());

        appendModalitiesInComboBox();
        cbxModalities.setValue(cbxModalities.getItems().get(0));
        generateGenderToggles();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        changeModalityAndGender();

        setOnActionToTeamsButtons();

        generateColumns();

        matchTableView.setRoot(rootMatch);
        groupTableView.setRoot(rootGroup);
    }

    private void generateColumns(){
        positionMatchColumn.setCellValueFactory(param -> param.getValue().getValue().positionProperty());
        versusColumn.setCellValueFactory(param -> param.getValue().getValue().versusProperty());
        teamAColumn.setCellValueFactory(param -> param.getValue().getValue().teamAProperty());
        teamBColumn.setCellValueFactory(param -> param.getValue().getValue().teamBProperty());
        scoreboardAColumn.setCellValueFactory(param -> param.getValue().getValue().scoreAProperty());
        scoreboardBColumn.setCellValueFactory(param -> param.getValue().getValue().scoreBProperty());

        positionGroupColumn.setCellValueFactory(param -> param.getValue().getValue().positionProperty());
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

    @FXML
    protected void changeModality(){
        genderGroup.getToggles().clear();
        paneGenders.getChildren().clear();
        generateGenderToggles();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));
        changeModalityAndGender();
    }

    private void setOnActionToTeamsButtons(){
        Stream<Node> toggleNodeTeams = gridTeams.getChildren().stream().filter(node ->
                node.getClass().getTypeName().equals(JFXToggleNode.class.getTypeName()));

        toggleNodeTeams.forEach(nodeTeam -> {
            JFXToggleNode btnTeam = (JFXToggleNode) nodeTeam;
            btnTeam.setOnAction(this::editTeam);
            btnTeam.getStyleClass().add("team_hover");
            btnTeam.setCursor(Cursor.HAND);
        });
    }

    @FXML
    protected void changeModalityAndGender(){
        lblModalityAndGender0.setText(getModalityAndGender().toLowerCase());
        lblModalityAndGender1.setText(getModalityAndGender().toLowerCase());
        lblModalityAndGender2.setText(getModalityAndGender().toLowerCase());
    }

    @FXML
    private void editTeam(ActionEvent event){
        clearTextFields();
        Team teamFound = findTeamByName();

        if(teamFound != null){
            txtTeamName.setText(teamFound.getName());
            txtTeamTag.setText(teamFound.getTag());
        }
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

    @FXML
    protected void deleteTeam(){
        if(findTeamByName() != null) {
            Team teamFound = findTeamByName();
            teams.remove(teamFound);
            getSelectedTeam().setText("");
            getSelectedTeam().getStyleClass().add("team_hover");
            clearTextFields();
        }
    }

    @FXML
    protected void saveTeam(){
        Team teamFound = findTeamByName();

        if(teamFound != null)
            updateTeam(teamFound);
        else
            createTeam();

        getSelectedTeam().setText(txtTeamName.getText());
        getSelectedTeam().getStyleClass().clear();
        listTeams();
    }

    private void updateTeam(Team teamFound){
        int teamIndex = teams.indexOf(teamFound);
        teams.get(teamIndex).setName(txtTeamName.getText());
        teams.get(teamIndex).setTag(txtTeamTag.getText());
    }

    private void createTeam(){
        teams.add(new Team(txtTeamName.getText(), txtTeamTag.getText()));
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

    private Team findTeamByName(){
        if(getSelectedTeam() != null)
            return teams.stream().filter(team ->
                    team.getName().equals(getSelectedTeam().getText())).findFirst().orElse(null);
        return null;
    }

    private void clearTextFields(){
        txtTeamName.clear();
        txtTeamTag.clear();
    }

    private JFXToggleNode getSelectedTeam(){
        return (JFXToggleNode) teamGroup.getSelectedToggle();
    }

    private void listTeams(){
        teams.forEach(team -> System.out.print(team.getName() + " "));
        System.out.println("\n");
    }

    @FXML
    private void loadGroupDialog(){
        String heading = "Agrupar times";
        String body = "Tem certeza que deseja agrupar os times de " + getModalityAndGender() + "?" +
                "\n(você não poderá mais alterar os times de " + getModalityAndGender() + ")";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, HomeController.staticStackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> dialog.close());
        dialog.show();
    }

    private String getModalityAndGender(){
        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();
        return cbxModalities.getValue().toString() + " " + rdb.getText();
    }
}
