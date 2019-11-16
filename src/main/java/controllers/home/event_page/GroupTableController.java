package controllers.home.event_page;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTreeTableView;
import controllers.home.HomeController;
import helpers.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import models.GeneralMatch;
import models.Match;
import models.Team;
import services.EventService;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

import static controllers.home.event_page.EventPageController.*;

public class GroupTableController implements Initializable {
    public JFXComboBox cbxGroups;
    public JFXButton btnGroupTeams;
    public JFXTreeTableView groupTableView;
    public TreeTableColumn<GroupTableModel, String> teamColumn;
    public TreeTableColumn<GroupTableModel, Number> pointsColumn, ownPointsColumn, againstPointsColumn,
            balanceColumn, foulsColumn;
    private TreeItem<GroupTableModel> rootGroup = new TreeItem<>(new GroupTableModel());

    public static List<List<Team>> groups;
    public static Integer groupIndex;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        generateGroupTableColumns();
        groupTableView.setRoot(rootGroup);

        if(!groups.isEmpty()) {
            changeNodeStates();
            showGroupsOnTable();
        }
    }

    private void changeNodeStates(){
        generateCbxGroupsItens();
        cbxGroups.setValue(cbxGroups.getItems().get(groupIndex != null ? groupIndex : 0));
        btnGroupTeams.setVisible(false);
    }

    @FXML
    protected void showGroupsOnTable(){
        groupIndex = (int) cbxGroups.getValue() - 1;
        rootGroup.getChildren().clear();

        groups.get(groupIndex).forEach(team -> {
            TreeItem<GroupTableModel> teamRow = new TreeItem<>(new GroupTableModel(
                    team.getName(), team.getScore().getPoints(), team.getScore().getOwnPoints(),
                    team.getScore().getAgainstPoints(), team.getScore().getBalance(), team.getScore().getFouls()));
            rootGroup.getChildren().add(teamRow);
        });
    }

    @FXML
    protected void loadGroupDialog(){
        String heading = strings.getString("groupTeams");
        String body = strings.getString("groupDialog.body1") +
                "\n" + MessageFormat.format(strings.getString("groupDialog.body2"), modalityAndGender);
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, HomeController.staticStackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            generateGroups();
            dialog.close();
        });

        if(!TeamsGridController.teams.isEmpty()) dialog.show();
        else HomeController.showToastMessage(strings.getString("error.addTeamsFirst"));
    }

    private void generateGroups(){
        TeamGroupsManager teamGroupsManager = new TeamGroupsManager();

        teamGroupsManager.groupAllTeamsByTag(TeamsGridController.teams, getTeamTags());
        groups = teamGroupsManager.generateGroupsAndReturn(3);
        actualGender.setTeams(teamGroupsManager.getOrderedTeams());
        generateMatches();

        new EventService().updateEvent(event.getId(), event);
        HomeController.loadEventPageView();
    }

    private void generateMatches(){
        List<Match> newMatches = new MatchesGenerator().generateMatchesAndReturn(groups);
        actualGender.setMatches(newMatches);
        addMatchesToEvent(newMatches);
    }

    private void addMatchesToEvent(List<Match> newMatches){
        for (Match match : newMatches)
            event.getMatches().get(0).add(new GeneralMatch(actualModality.getName(),
                    actualGender.getName(), match.getStage(), match.getTeams().get(0), match.getTeams().get(1)));
    }

    private void generateCbxGroupsItens(){
        IntStream.range(0, groups.size()).forEachOrdered(x -> cbxGroups.getItems().add(x + 1));
    }

    private void generateGroupTableColumns(){
        teamColumn.setCellValueFactory(param -> param.getValue().getValue().teamProperty());
        pointsColumn.setCellValueFactory(param -> param.getValue().getValue().pointsProperty());
        ownPointsColumn.setCellValueFactory(param -> param.getValue().getValue().ownPointsProperty());
        againstPointsColumn.setCellValueFactory(param -> param.getValue().getValue().againstPointsProperty());
        balanceColumn.setCellValueFactory(param -> param.getValue().getValue().balanceProperty());
        foulsColumn.setCellValueFactory(param -> param.getValue().getValue().foulsProperty());
    }

    private List<String> getTeamTags(){
        List<String> tags = new ArrayList<>();
        TeamsGridController.teams.forEach(team -> {
            if(!tags.contains(team.getTag())) tags.add(team.getTag());
        });
        return tags;
    }
}
