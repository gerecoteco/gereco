package controllers.home.event_page;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import controllers.home.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import models.Team;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.home.event_page.EventPageController.modalityAndGender;

public class TeamsGridController implements Initializable {
    public GridPane gridTeams;
    private ToggleGroup teamGroup;
    public VBox vBoxTeamButtons;
    public VBox vboxTeamOptions;
    public JFXTextField txtTeamName;
    public JFXTextField txtTeamTag;
    public Label lblModalityAndGender;

    static List<Team> teams;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        teams = new ArrayList<>();
        lblModalityAndGender.setText(modalityAndGender);
        teamGroup = new ToggleGroup();

        if(!EventPageController.actualGender.getTeams().isEmpty()) {
            teams = EventPageController.actualGender.getTeams();
            vboxTeamOptions.getChildren().remove(vBoxTeamButtons);
            txtTeamName.setEditable(false);
            txtTeamTag.setEditable(false);
        }

        generateTeamToggles();
    }

    private void generateTeamToggles(){
        final int COLUMNS = 5, ROWS = 4;
        int teamIndex = 0;

        for(int x = 0; x < ROWS; x++){
            for(int y = 0; y < COLUMNS; y++){
                if(teams.isEmpty() || teamIndex < teams.size()){
                    gridTeams.add(createToggleTeamAndReturn(teamIndex), y, x);
                    teamIndex++;
                } else return;
            }
        }
    }

    private JFXToggleNode createToggleTeamAndReturn(int teamIndex){
        JFXToggleNode toggleTeam = new JFXToggleNode();
        toggleTeam.setOnAction(this::editTeam);
        toggleTeam.setToggleGroup(teamGroup);
        toggleTeam.setText(teamIndex < teams.size() ? teams.get(teamIndex).getName() : "");
        toggleTeam.getStyleClass().add(teams.isEmpty() ? "add_team" : "hover_team");

        return toggleTeam;
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
        if(getSelectedTeam() != null && findTeamByName() != null) {
            Team teamFound = findTeamByName();
            teams.remove(teamFound);
            getSelectedTeam().setText("");
            getSelectedTeam().getStyleClass().add("add_team");
            clearTextFields();
        } else HomeController.showToastMessage(strings.getString("error.selectTeamFirst"));
    }

    @FXML
    protected void saveTeam(){
        if(getSelectedTeam() != null){
            Team teamFound = findTeamByName();

            if(teamFound == null){
                if(!txtTeamName.getText().equals("")){
                    createTeam();
                    getSelectedTeam().getStyleClass().clear();
                    getSelectedTeam().getStyleClass().add("hover_team");
                } else
                    HomeController.showToastMessage(strings.getString("error.teamMustContainName"));
            } else updateTeam(teamFound);

            getSelectedTeam().setText(txtTeamName.getText());
        } else HomeController.showToastMessage(strings.getString("error.selectTeamFirst"));
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
