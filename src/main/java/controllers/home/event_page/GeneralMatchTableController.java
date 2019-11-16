package controllers.home.event_page;

import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXTreeTableView;
import controllers.home.HomeController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import models.GeneralMatch;
import services.EventService;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static controllers.home.event_page.EventPageController.event;

public class GeneralMatchTableController implements Initializable {
    public JFXTreeTableView matchTableView;
    public TreeTableColumn<GeneralMatch, String> modalityColumn, genderColumn,versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<GeneralMatch, Number> stageColumn;
    public JFXToggleNode btnTab1, btnTab2;
    private ToggleGroup tabGroup;
    private TreeItem<GeneralMatch> rootMatch = new TreeItem<>(new GeneralMatch());

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private int rowIndex = 0;
    private int tabIndex = 0;
    private TreeItem<GeneralMatch> draggedItem;
    private ObservableList<TreeItem<GeneralMatch>> nextItems;

    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        tabGroup = new ToggleGroup();
        tabGroup.getToggles().addAll(btnTab1, btnTab2);
        tabGroup.selectToggle(tabGroup.getToggles().get(tabIndex));

        matchTableView.setRoot(rootMatch);
        generateColumns();
        setRowFactoryOfTable();
        loadAllEventMatches();
    }

    @FXML
    protected void changeTab(){
        tabIndex = tabGroup.getToggles().indexOf(tabGroup.getSelectedToggle());
        loadAllEventMatches();
    }

    @FXML
    protected void handleBtnChangeMatchToAnotherTab(){
        if(!matchTableView.getSelectionModel().isEmpty()) changeMatchToAnotherTab();
        else HomeController.showToastMessage(strings.getString("selectMatchFirst"));
    }

    private void changeMatchToAnotherTab(){
        int selectedIndex = matchTableView.getSelectionModel().getSelectedIndex();
        GeneralMatch selectedMatch = rootMatch.getChildren().get(selectedIndex).getValue();

        event.getMatches().get(tabIndex).remove(selectedIndex);
        tabIndex = tabIndex == 0 ? 1 : 0;
        event.getMatches().get(tabIndex).add(selectedMatch);

        tabGroup.selectToggle(tabGroup.getToggles().get(tabIndex));
        loadAllEventMatches();
        matchTableView.getSelectionModel().select(rootMatch.getChildren().size() - 1);

        new EventService().updateEventMatches(event.getId(), event.getMatches());
    }

    private void loadAllEventMatches(){
        rootMatch.getChildren().clear();
        event.getMatches().get(tabIndex).forEach(match -> rootMatch.getChildren().add(new TreeItem<>(match)));
    }

    private void generateColumns(){
        modalityColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getModality()));
        genderColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getGender()));
        versusColumn.setCellValueFactory(param ->
                new SimpleStringProperty("X"));
        stageColumn.setCellValueFactory(param ->
                new SimpleIntegerProperty(param.getValue().getValue().getStage()));
        stageColumn.setCellFactory(tc -> new TreeTableCell<GeneralMatch, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty) ;
                setText(empty || item.intValue() == 0 ? null : Integer.toString(item.intValue()));
            }
        });
        teamAColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getTeamA()));
        teamBColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getTeamB()));
    }

    private void setRowFactoryOfTable(){
        matchTableView.setRowFactory(tv -> {
            TreeTableRow<GeneralMatch> row = new TreeTableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();
                    draggedItem = rootMatch.getChildren().get(index);

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    rootMatch.getChildren().remove(draggedItem);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                matchTableView.getSelectionModel().clearSelection();

                rootMatch.getChildren().removeIf(item -> item.getValue().getModality() == null);

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    nextItems = FXCollections.observableArrayList();

                    for (int x = row.getIndex(); x < rootMatch.getChildren().size(); x++)
                        nextItems.add(rootMatch.getChildren().get(x));

                    resetMatchTableItens();
                    matchTableView.getSelectionModel().select(row.getIndex());

                    rowIndex = row.getIndex();
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(e -> {
                Dragboard db = e.getDragboard();

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int dropIndex = row.isEmpty() ? rootMatch.getChildren().size() : row.getIndex();

                    if(dropIndex != rootMatch.getChildren().size()){
                        rootMatch.getChildren().get(rowIndex).setValue(draggedItem.getValue());
                        matchTableView.getSelectionModel().select(dropIndex);
                    } else {
                        rootMatch.getChildren().add(draggedItem);
                        matchTableView.getSelectionModel().select(rootMatch.getChildren().size()-1);
                    }

                    e.setDropCompleted(true);
                    e.consume();
                }
            });

            row.setOnDragDone(e -> handleOnDragDone());

            return row ;
        });
    }

    private void handleOnDragDone(){
        int selectedIndex = matchTableView.getSelectionModel().getSelectedIndex();
        boolean invalidDrop = event.getMatches().get(tabIndex).size() != rootMatch.getChildren().size() ||
                rootMatch.getChildren().stream().anyMatch(item -> item.getValue().getModality() == null);

        if(invalidDrop){
            loadAllEventMatches();
            matchTableView.getSelectionModel().select(selectedIndex);
        } else
            updateEventMatches();
    }

    private void updateEventMatches(){
        event.getMatches().get(tabIndex).clear();
        rootMatch.getChildren().forEach(match -> {
            GeneralMatch generalMatch = match.getValue();
            event.getMatches().get(tabIndex).add(generalMatch);
        });

        new EventService().updateEventMatches(event.getId(), event.getMatches());
    }

    private void resetMatchTableItens(){
        rootMatch.getChildren().removeAll(nextItems);
        rootMatch.getChildren().add(new TreeItem<>(new GeneralMatch()));
        rootMatch.getChildren().addAll(nextItems);
    }
}

