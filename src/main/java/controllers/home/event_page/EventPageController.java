package controllers.home.event_page;

import com.jfoenix.controls.*;
import controllers.home.event_list.EventItemController;
import controllers.home.event_list.EventListController;
import helpers.UTF8Control;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Gender;
import models.Modality;
import models.Event;
import models.Team;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EventPageController implements Initializable {
    public Label lblEventName;
    public JFXComboBox cbxModalities;
    public HBox paneGenders;
    public AnchorPane paneManager;
    public AnchorPane paneTeamGrid;
    public AnchorPane paneGroupTable;
    public AnchorPane paneMatchTable;
    public AnchorPane paneGeneralMatchTable;
    public AnchorPane paneLeaderBoard;
    private ToggleGroup genderGroup;

    public static Event event;
    public static Gender actualGender;
    public static String modalityAndGender;

    private static Modality actualModality;
    private static int genderIndex;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        genderGroup = new ToggleGroup();
        event = findEventById();
        lblEventName.setText(event.getName());

        appendModalitiesInComboBox();
        if(actualGender != null){
            cbxModalities.setValue(actualModality.getName());
            generateGenderToggles();
            genderGroup.selectToggle(genderGroup.getToggles().get(genderIndex));
        } else {
            cbxModalities.setValue(cbxModalities.getItems().get(0));
            generateGenderToggles();
            genderGroup.selectToggle(genderGroup.getToggles().get(0));
        }

        changeModalityAndGender();
        loadEventManagerViews();
    }

    private void loadEventManagerViews(){
        loadTeamGridView();
        loadGroupTableView();
        loadMatchTableView();
        loadGeneralMatchTableView();
        loadLoaderBoardView();
    }

    private void generateGenderToggles(){
        getSelectedModality().getGenders().forEach(gender -> {
            JFXRadioButton rdbGender = new JFXRadioButton(strings.getString(gender.getName()));
            rdbGender.setOnAction(e -> changeModalityAndGender());
            rdbGender.setToggleGroup(genderGroup);
            paneGenders.getChildren().add(rdbGender);
        });
    }

    private Modality getSelectedModality(){
        return event.getModalities().stream().filter(modality ->
                modality.getName().equals(cbxModalities.getValue().toString())).findAny().orElse(null);
    }

    private Gender getSelectedGender(){
        String rdbText = ((JFXRadioButton) genderGroup.getSelectedToggle()).getText();
        String genderName = rdbText.equals(strings.getString("male")) ? "male" :
                rdbText.equals(strings.getString("female")) ? "female" : "mixed";

        return getSelectedModality().getGenders().stream().filter(gender ->
                gender.getName().equals(genderName)).findAny().orElse(null);
    }

    @FXML
    protected void changeModality(){
        actualModality = getSelectedModality();

        genderGroup.getToggles().clear();
        paneGenders.getChildren().clear();
        generateGenderToggles();
        genderGroup.selectToggle(genderGroup.getToggles().get(0));

        changeModalityAndGender();
    }

    private void changeModalityAndGender(){
        actualGender = getSelectedGender();
        actualModality = getSelectedModality();
        modalityAndGender = getModalityAndGender();
        genderIndex = genderGroup.getToggles().indexOf(genderGroup.getSelectedToggle());
        GroupTableController.groupIndex = 0;

        loadEventManagerViews();
    }

    private Event findEventById(){
        return EventListController.institutionEvents.stream().filter(
                event -> event.getId().equals(EventItemController.eventId)).findFirst().orElse(null);
    }

    private void appendModalitiesInComboBox(){
        List<String> modalities = new ArrayList<>();
        event.getModalities().forEach(modality -> modalities.add(modality.getName()));

        cbxModalities.setItems(FXCollections.observableList(modalities));
    }

    private String getModalityAndGender(){
        return cbxModalities.getValue().toString() + " (" + strings.getString(actualGender.getName()) + ")";
    }

    private void loadTeamGridView(){
        paneTeamGrid.getChildren().clear();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/event_page/teams-grid.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            paneTeamGrid.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGroupTableView(){
        GroupTableController.groups = actualGender.getTeams().isEmpty() ?
                new ArrayList<>() : getGenderGroups();

        paneGroupTable.getChildren().clear();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/event_page/group-table.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            paneGroupTable.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoaderBoardView(){
        paneLeaderBoard.getChildren().clear();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/event_page/leaderboard.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            paneLeaderBoard.getChildren().add(root);

            setPaneAnchorsToZero(paneLeaderBoard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadMatchTableView(){
        paneMatchTable.getChildren().clear();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/event_page/match-table.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            paneMatchTable.getChildren().add(root);

            setPaneAnchorsToZero(paneMatchTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadGeneralMatchTableView(){
        paneGeneralMatchTable.getChildren().clear();
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/event_page/general-match-table.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            paneGeneralMatchTable.getChildren().add(root);

            setPaneAnchorsToZero(paneGeneralMatchTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPaneAnchorsToZero(AnchorPane pane){
        AnchorPane.setBottomAnchor(pane.getChildren().get(0), 0.0);
        AnchorPane.setTopAnchor(pane.getChildren().get(0), 0.0);
        AnchorPane.setRightAnchor(pane.getChildren().get(0), 0.0);
        AnchorPane.setRightAnchor(pane.getChildren().get(0), 0.0);
    }

    static List<List<Team>> getGenderGroups(){
        int lastGroupIndex = actualGender.getTeams().stream()
                .max(Comparator.comparing(Team::getGroup))
                .get().getGroup();
        List<List<Team>> groups = IntStream.range(0, lastGroupIndex+1)
                .<List<Team>>mapToObj(x -> new ArrayList<>()).collect(Collectors.toList());

        actualGender.getTeams().forEach(team -> groups.get(team.getGroup()).add(team));
        return groups;
    }
}
