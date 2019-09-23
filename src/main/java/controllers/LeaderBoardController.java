package controllers;

import com.jfoenix.controls.JFXTreeTableView;
import helpers.LeaderBoardView;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import models.Team;

import java.util.Comparator;
import java.util.List;

import static controllers.EventPageController.actualGender;

public class LeaderBoardController {
    public JFXTreeTableView leaderBoardTableView;
    public TreeTableColumn<LeaderBoardView, String> nameColumn;
    public TreeTableColumn<LeaderBoardView, Number> positionColumn, pointsColumn, ownPointsColumn,
            againstPoinstColumn, balanceColumn, foulsColumn;
    private TreeItem<LeaderBoardView> rootLeaderBoard = new TreeItem<>(new LeaderBoardView());

    @FXML
    public void initialize(){
        leaderBoardTableView.setRoot(rootLeaderBoard);
        generateColumns();

        if(!actualGender.getTeams().isEmpty()) listTeamsOnTable();
    }

    private void listTeamsOnTable(){
        rootLeaderBoard.getChildren().clear();
        List<Team> teams = actualGender.getTeams();

        teams.sort(Comparator.comparing((Team team) -> team.getScore().getPoints())
                .thenComparing((Team team) -> team.getScore().getOwnPoints())
                .thenComparing((Team team) -> team.getScore().getBalance())
                .thenComparing((Team team) -> team.getScore().getFouls()).reversed());

        for(int x=0; x < teams.size(); x++){
            TreeItem<LeaderBoardView> teamRow = new TreeItem<>(new LeaderBoardView(
                    x+1, teams.get(x).getName(), teams.get(x).getScore().getPoints(),
                    teams.get(x).getScore().getOwnPoints(), teams.get(x).getScore().getAgainstPoints(),
                    teams.get(x).getScore().getBalance(), teams.get(x).getScore().getFouls()));
            rootLeaderBoard.getChildren().add(teamRow);
        }
    }

    private void generateColumns(){
        positionColumn.setCellValueFactory(param -> param.getValue().getValue().positionProperty());
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
        pointsColumn.setCellValueFactory(param -> param.getValue().getValue().pointsProperty());
        ownPointsColumn.setCellValueFactory(param -> param.getValue().getValue().ownPointsProperty());
        againstPoinstColumn.setCellValueFactory(param -> param.getValue().getValue().againstPointsProperty());
        balanceColumn.setCellValueFactory(param -> param.getValue().getValue().balanceProperty());
        foulsColumn.setCellValueFactory(param -> param.getValue().getValue().foulsProperty());
    }
}
