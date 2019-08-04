package controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Team;

import java.util.ArrayList;
import java.util.List;

public class EventCreateController {
    public JFXChipView chipModalities;
    public JFXComboBox cbxModalities;
    public JFXTextField txtTeamName;
    public JFXTextField txtTeamTag;
    public GridPane gridTeams;
    public ToggleGroup teamGroup;
    public ToggleGroup genderGroup;
    public Label lblModalityAndGender;

    private List<Team> teams;

    @FXML
    public void initialize() {
        teams = new ArrayList<>();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));

        setOnActionToTeamsButtons();
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
    protected void appendModalitiesOnComboBox(){
        if(!chipModalities.getChips().isEmpty()){
            cbxModalities.setItems(chipModalities.getChips());
            chipModalities.setDisable(true);
        }
    }

    @FXML
    protected void changeModalityAndGender(){
        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();

        if(cbxModalities.getValue() != null){
            String modalityAndGender = cbxModalities.getValue().toString() + " " + rdb.getText();
            lblModalityAndGender.setText(modalityAndGender.toLowerCase());
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
    protected void cancelEventCreation(ActionEvent event){
        Button btnCancel = (Button) event.getSource();
        Stage actualStage = (Stage) btnCancel.getScene().getWindow();
        actualStage.close();
    }

    private Team findTeamByName(){
        if(teamGroup.getSelectedToggle() != null){
            return teams.stream().filter(team ->
                    team.getName().equals(getSelectedTeam().getText())).findFirst().orElse(null);
        }
        return null;
    }

    private void listTeams(){
        teams.forEach(team -> System.out.print(team.getName() + " "));
        System.out.println("\n");
    }

    private void clearTextFields(){
        txtTeamName.clear();
        txtTeamTag.clear();
    }

    private JFXToggleNode getSelectedTeam(){
        return (JFXToggleNode) teamGroup.getSelectedToggle();
    }
}
