package controllers.home.event_page;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXTreeTableView;
import controllers.home.HomeController;
import helpers.LeaderBoardModel;
import helpers.PdfTableGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.ScrollEvent;
import javafx.stage.DirectoryChooser;
import models.Score;
import models.Team;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

import static application.Main.mainStage;
import static controllers.home.event_page.EventPageController.*;

public class LeaderBoardController implements Initializable {
    public JFXTreeTableView leaderBoardTableView;
    public JFXToggleNode tabModalityAndGender;
    public JFXToggleNode tabGeneral;
    public TreeTableColumn<LeaderBoardModel, String> nameColumn;
    public TreeTableColumn<LeaderBoardModel, Number> positionColumn, pointsColumn, ownPointsColumn,
            againstPoinstColumn, balanceColumn, foulsColumn;
    private TreeItem<LeaderBoardModel> rootLeaderBoard = new TreeItem<>(new LeaderBoardModel());
    private ToggleGroup leaderBoardTabs;

    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        tabModalityAndGender.setText(modalityAndGender);
        initializeLeaderBoardTabs();
        generateLeaderBoardTable();
    }

    private void generateLeaderBoardTable(){
        leaderBoardTableView.setRoot(rootLeaderBoard);
        generateColumns();
        blockTableHozizontalScroll();

        if(!actualGender.getTeams().isEmpty()) listTeamsOnTable();
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

    private void blockTableHozizontalScroll(){
        leaderBoardTableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) event.consume();
        });
    }

    @FXML
    protected void changeTab(ActionEvent event){
        JFXToggleNode toggleNode = (JFXToggleNode) event.getSource();
        if(!toggleNode.isSelected()) toggleNode.setSelected(true);
        listTeamsOnTable();
    }

    private void initializeLeaderBoardTabs(){
        leaderBoardTabs = new ToggleGroup();
        leaderBoardTabs.getToggles().addAll(tabModalityAndGender, tabGeneral);
        leaderBoardTabs.selectToggle(tabModalityAndGender);
    }

    private List<Team> calculateGeneralTeamsScoreAndReturn(){
        List<String> allTeamNames = new ArrayList<>();
        List<Team> allTeams = new ArrayList<>();
        List<Team> generalTeams = new ArrayList<>();

        generateAllEventTeams(allTeamNames, allTeams);
        allTeamNames.forEach(teamName -> {
            Team generalTeam = new Team(teamName);
            Score generalTeamScore = generalTeam.getScore();
            allTeams.forEach(team -> {
                Score actualTeamScore = team.getScore();
                if(team.getName().equals(generalTeam.getName())){
                    generalTeamScore.setPoints(generalTeamScore.getPoints() + actualTeamScore.getPoints());
                    generalTeamScore.setOwnPoints(generalTeamScore.getOwnPoints() + actualTeamScore.getOwnPoints());
                    generalTeamScore.setAgainstPoints(generalTeamScore.getAgainstPoints() + actualTeamScore.getAgainstPoints());
                    generalTeamScore.setBalance(generalTeamScore.getOwnPoints() - generalTeamScore.getAgainstPoints());
                    generalTeamScore.setFouls(generalTeamScore.getFouls() + actualTeamScore.getFouls());
                }
            });
            generalTeams.add(generalTeam);
        });

        return generalTeams;
    }

    private void generateAllEventTeams(List<String> allTeamNames, List<Team> allTeams){
        event.getModalities().forEach(modality ->{
            modality.getGenders().forEach(gender -> {
                if(!gender.getTeams().isEmpty()){
                    allTeams.addAll(gender.getTeams());
                    gender.getTeams().forEach(team -> {
                        if(!allTeamNames.contains(team.getName()))
                            allTeamNames.add(team.getName());
                    });
                }
            });
        });
    }

    private void listTeamsOnTable(){
        rootLeaderBoard.getChildren().clear();
        List<Team> teams = leaderBoardTabs.getSelectedToggle().equals(tabGeneral) ?
                calculateGeneralTeamsScoreAndReturn() : actualGender.getTeams();

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
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File choosedDirectory = directoryChooser.showDialog(mainStage);

        PdfTableGenerator pdfTableGenerator = new PdfTableGenerator();
        JFXToggleNode selectedTab = (JFXToggleNode) leaderBoardTabs.getSelectedToggle();
        String title = selectedTab.equals(tabModalityAndGender) ?
                MessageFormat.format(strings.getString("leaderboardTitle.modalityAndGender"),
                        modalityAndGender, event.getName()) :
                MessageFormat.format(strings.getString("leaderboardTitle.general"), event.getName());
        String fileName = strings.getString("leaderboard") + " - " + selectedTab.getText() +
                " - " + LocalDate.now() + ".pdf";

        pdfTableGenerator.generateLeaderBoardPdf(
                title, choosedDirectory + "/" + fileName, rootLeaderBoard.getChildren());
        HomeController.showToastMessage(strings.getString("successDownloadPDF"));
    }
}
