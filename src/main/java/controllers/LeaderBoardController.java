package controllers;

import com.jfoenix.controls.JFXTreeTableView;
import helpers.LeaderBoardView;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

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
