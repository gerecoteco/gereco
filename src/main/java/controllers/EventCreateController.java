package controllers;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
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

        for(Node nodeTeam : gridTeams.getChildren()){
            if(nodeTeam.getClass().getTypeName().equals(JFXToggleNode.class.getTypeName())){
                JFXToggleNode btnTeam = (JFXToggleNode) nodeTeam;
                btnTeam.setOnAction(this::editTeam);
            }
        }
    }

    @FXML
    protected void listChips(){
        cbxModalities.setItems(chipModalities.getChips());
        chipModalities.setDisable(true);
    }

    @FXML
    protected void changeModalityAndGender(){
        JFXRadioButton rdb = (JFXRadioButton) genderGroup.getSelectedToggle();
        String modalityAndGender = cbxModalities.getValue().toString() + " " + rdb.getText();
        lblModalityAndGender.setText(modalityAndGender.toLowerCase());
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
        JFXToggleNode selectedToggle = (JFXToggleNode) teamGroup.getSelectedToggle();
        Team teamFound = findTeamByName();

        if(teamFound != null){
            int teamIndex = teams.indexOf(teamFound);
            teams.get(teamIndex).setName(txtTeamName.getText());
            teams.get(teamIndex).setTag(txtTeamTag.getText());
        } else {
            teams.add(new Team(txtTeamName.getText(), txtTeamTag.getText()));
            selectedToggle.setText(txtTeamName.getText());
        }

        clearTextFields();
        listTeams();
    }

    @FXML
    protected void deleteTeam(){
        JFXToggleNode btn = (JFXToggleNode) teamGroup.getSelectedToggle();
        if(findTeamByName() != null) {
            Team teamFound = findTeamByName();
            teams.remove(teamFound);
            btn.setText("");
            clearTextFields();
        }
    }

    private Team findTeamByName(){
        if(teamGroup.getSelectedToggle() != null){
            JFXToggleNode selectedToggle = (JFXToggleNode) teamGroup.getSelectedToggle();
            return teams.stream().filter(team ->
                    team.getName().equals(selectedToggle.getText())).findFirst().orElse(null);
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
}
