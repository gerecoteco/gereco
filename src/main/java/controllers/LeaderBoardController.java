package controllers;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXTreeTableView;
import helpers.LeaderBoardView;
import helpers.PdfTableGenerator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import models.Team;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static controllers.EventPageController.*;

public class LeaderBoardController {
    public JFXTreeTableView leaderBoardTableView;
    public Label lblModalityAndGender;
    public TreeTableColumn<LeaderBoardView, String> nameColumn;
    public TreeTableColumn<LeaderBoardView, Number> positionColumn, pointsColumn, ownPointsColumn,
            againstPoinstColumn, balanceColumn, foulsColumn;
    private TreeItem<LeaderBoardView> rootLeaderBoard = new TreeItem<>(new LeaderBoardView());

    @FXML
    public void initialize(){
        lblModalityAndGender.setText(modalityAndGender);

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

    @FXML
    protected void exportLeaderBoardToPdf() throws IOException, DocumentException {
        PdfTableGenerator pdfTableGenerator = new PdfTableGenerator();
        String title = "Classificação do " + modalityAndGender + " do " + event.getName();
        pdfTableGenerator.createPdf(title, "../../../classificacao-gereco.pdf", rootLeaderBoard.getChildren());

        HomeController.showToastMessage("A classificação foi salva com sucesso!");
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
