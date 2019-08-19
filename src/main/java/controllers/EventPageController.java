package controllers;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
    public JFXTextField txtTeamName;
    public JFXTextField txtTeamTag;
    public AnchorPane paneManager;
    public Label lblModalityAndGender0, lblModalityAndGender1, lblModalityAndGender2;

    private Event event;
    private List<Team> teams;

    @FXML
    public void initialize() {
        teams = new ArrayList<>();
        event = findEventByName();
        lblEventName.setText(event.getName());
        genderGroup.selectToggle(genderGroup.getToggles().get(0));

        appendModalitiesInComboBox();
        cbxModalities.setValue(cbxModalities.getItems().get(0));
        setOnActionToTeamsButtons();
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
        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();

        String modalityAndGender = cbxModalities.getValue().toString() + " " + rdb.getText();
        lblModalityAndGender0.setText(modalityAndGender.toLowerCase());
        lblModalityAndGender1.setText(modalityAndGender.toLowerCase());
        lblModalityAndGender2.setText(modalityAndGender.toLowerCase());
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
            stage.initModality(Modality.APPLICATION_MODAL);
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

    private Event findEventByName(){
        return EventListController.institutionEvents.stream().filter(
                event -> event.getName().equals(EventItemController.eventName)).findFirst().orElse(null);
    }

    private void appendModalitiesInComboBox(){
        List<String> modalities = new ArrayList<>();
        event.getModalities().forEach(modality -> modalities.add(modality.getName()));

        cbxModalities.setItems(FXCollections.observableList(modalities));
    }

    private Team findTeamByName(){
        if(teamGroup.getSelectedToggle() != null)
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
    protected void loadGroupDialog(){
        JFXDialogLayout content = new JFXDialogLayout();
        HBox hbox = new HBox(5);
        JFXDialog dialog;

        JFXButton btnConfirm = new JFXButton("Sim");
        JFXButton btnCancel = new JFXButton("Cancelar");

        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();
        String modalityAndGender = cbxModalities.getValue().toString() + " " + rdb.getText();

        content.setHeading(new Label("Agrupar times"));
        content.setBody(new Text("Tem certeza que deseja agrupar os times de " + modalityAndGender + "?" +
                "\n(você não poderá mais alterar os times de " + modalityAndGender + ")"));

        hbox.getChildren().add(btnCancel);
        hbox.getChildren().add(btnConfirm);
        content.setActions(hbox);

        btnCancel.setStyle("-fx-background-color: #369137");
        btnConfirm.setStyle("-fx-background-color: #369137");

        dialog = new JFXDialog(HomeController.staticStackPaneMain, content, JFXDialog.DialogTransition.CENTER);

        btnCancel.setOnAction(action -> dialog.close());
        dialog.show();
    }
}
