package controllers.home.event_page;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXTreeTableView;
import controllers.home.HomeController;
import helpers.LeaderBoardModel;
import helpers.PdfTableGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import models.Team;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.home.event_page.EventPageController.*;

public class LeaderBoardController implements Initializable {
    public JFXTreeTableView leaderBoardTableView;
    public Label lblModalityAndGender;
    public TreeTableColumn<LeaderBoardModel, String> nameColumn;
    public TreeTableColumn<LeaderBoardModel, Number> positionColumn, pointsColumn, ownPointsColumn,
            againstPoinstColumn, balanceColumn, foulsColumn;
    private TreeItem<LeaderBoardModel> rootLeaderBoard = new TreeItem<>(new LeaderBoardModel());

    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
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
            TreeItem<LeaderBoardModel> teamRow = new TreeItem<>(new LeaderBoardModel(
                    x+1, teams.get(x).getName(), teams.get(x).getScore().getPoints(),
                    teams.get(x).getScore().getOwnPoints(), teams.get(x).getScore().getAgainstPoints(),
                    teams.get(x).getScore().getBalance(), teams.get(x).getScore().getFouls()));
            rootLeaderBoard.getChildren().add(teamRow);
        }
    }

    @FXML
    protected void exportLeaderBoardToPdf() throws IOException, DocumentException {
        PdfTableGenerator pdfTableGenerator = new PdfTableGenerator();
        String title = MessageFormat.format(strings.getString("leaderboardTitle"),
                modalityAndGender, event.getName());
        pdfTableGenerator.createPdf(title, "../../../classificacao-gereco.pdf", rootLeaderBoard.getChildren());

        HomeController.showToastMessage(strings.getString("successDownloadPDF"));
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
