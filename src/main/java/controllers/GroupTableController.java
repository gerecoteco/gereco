package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTreeTableView;
import helpers.DialogBuilder;
import helpers.TeamGroupsManager;
import helpers.groupTable.GroupTableView;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import models.Team;
import services.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static controllers.EventPageController.*;

public class GroupTableController {
    public JFXComboBox cbxGroups;
    public JFXButton btnGroupTeams;
    public JFXTreeTableView groupTableView;
    public TreeTableColumn<GroupTableView, String> teamCollumn;
    public TreeTableColumn<GroupTableView, Number> ownPointsColumn, againstPointsColumn, balanceColumn, foulsColumn;
    private TreeItem<GroupTableView> rootGroup = new TreeItem<>(new GroupTableView());

    static List<List<Team>> groups;

    @FXML
    public void initialize() {
        generateGroupTableColumns();
        groupTableView.setRoot(rootGroup);

        if(!groups.isEmpty()){
            generateCbxGroupsItens();
            cbxGroups.setValue(cbxGroups.getItems().get(0));
            showGroupsOnTable();
            btnGroupTeams.setDisable(true);
        }
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

    @FXML
    protected void loadGroupDialog(){
        String heading = "Agrupar times";
        String body = "Tem certeza que deseja agrupar os times de " + modalityAndGender + "?" +
                "\n(você não poderá mais alterar os times de " + modalityAndGender + ")";
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
        actualGender.setTeams(teamGroupsManager.getOrderedTeams());

        new EventService().updateEvent(EventItemController.eventId, event);
        generateCbxGroupsItens();
        cbxGroups.setValue(1);
        TeamsGridController.hideButtons();
    }

    private void generateCbxGroupsItens(){
        IntStream.range(0, groups.size()).forEachOrdered(x -> cbxGroups.getItems().add(x + 1));
    }

    private void generateGroupTableColumns(){
        teamCollumn.setCellValueFactory(param -> param.getValue().getValue().teamProperty());
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
