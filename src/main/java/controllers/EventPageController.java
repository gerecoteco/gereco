package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Event;
import models.Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventPageController {
    public Label lblEventName;
    public JFXComboBox cbxModalities;
    public GridPane gridTeams;
    public ToggleGroup teamGroup;
    public ToggleGroup genderGroup;
    public JFXTextField txtTeamName;
    public JFXTextField txtTeamTag;
    public Label lblModalityAndGender0, lblModalityAndGender1, lblModalityAndGender2, lblModalityAndGender3;

    private Event event;
    private List<Team> teams;

    @FXML
    public void initialize() {
        teams = new ArrayList<>();
        event = findEventByName();
        lblEventName.setText(event.getName());
        genderGroup.selectToggle(genderGroup.getToggles().get(0));

        setOnActionToTeamsButtons();
        appendModalitiesInComboBox();
    }

    private void setOnActionToTeamsButtons(){
        for(Node nodeTeam : gridTeams.getChildren()){
            if(nodeTeam.getClass().getTypeName().equals(JFXToggleNode.class.getTypeName())){
                JFXToggleNode btnTeam = (JFXToggleNode) nodeTeam;
                btnTeam.setOnAction(this::editTeam);
            }
        }
    }

    @FXML
    protected void changeModalityAndGender(){
        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();

        if(cbxModalities.getValue() != null){
            String modalityAndGender = cbxModalities.getValue().toString() + " " + rdb.getText();
            lblModalityAndGender0.setText(modalityAndGender.toLowerCase());
            lblModalityAndGender1.setText(modalityAndGender.toLowerCase());
            lblModalityAndGender2.setText(modalityAndGender.toLowerCase());
            lblModalityAndGender3.setText(modalityAndGender.toLowerCase());
        }
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
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/match-form.fxml")));
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
        if(teamGroup.getSelectedToggle() != null){
            return teams.stream().filter(team ->
                    team.getName().equals(getSelectedTeam().getText())).findFirst().orElse(null);
        }
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
}
