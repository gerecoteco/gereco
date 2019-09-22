package controllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TeamsGridController {
    public GridPane gridTeams;
    public ToggleGroup teamGroup;
    public VBox vBoxTeamButtons;
    public JFXTextField txtTeamName;
    public JFXTextField txtTeamTag;

    static List<Team> teams;

    @FXML
    public void initialize() {
        teams = new ArrayList<>();
        setOnActionToTeamsButtons();
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
    private void editTeam(ActionEvent event){
        clearTextFields();
        Team teamFound = findTeamByName();

        if(teamFound != null){
            txtTeamName.setText(teamFound.getName());
            txtTeamTag.setText(teamFound.getTag());
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
    }

    private void updateTeam(Team teamFound){
        int teamIndex = teams.indexOf(teamFound);
        teams.get(teamIndex).setName(txtTeamName.getText());
        teams.get(teamIndex).setTag(txtTeamTag.getText());
    }

    private void createTeam(){
        teams.add(new Team(txtTeamName.getText(), txtTeamTag.getText()));
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
}
