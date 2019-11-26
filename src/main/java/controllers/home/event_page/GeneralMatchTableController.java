package controllers.home.event_page;

import com.itextpdf.text.DocumentException;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXTreeTableView;
import controllers.home.HomeController;
import helpers.PdfTableGenerator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import models.GeneralMatch;
import services.EventService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static controllers.home.HomeController.openDirectoryChooserAndReturnDirectory;
import static controllers.home.event_page.EventPageController.event;
import static controllers.home.event_page.EventPageController.modalityAndGender;

public class GeneralMatchTableController implements Initializable {
    public JFXTreeTableView generalMatchTableView;
    public TreeTableColumn<GeneralMatch, String> modalityColumn, genderColumn,versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<GeneralMatch, Number> stageColumn;
    public JFXToggleNode btnTab1, btnTab2;
    private ToggleGroup tabGroup;
    private TreeItem<GeneralMatch> rootGeneralMatch = new TreeItem<>(new GeneralMatch());

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

        generateGeneralMatchTable();
    }

    private void generateGeneralMatchTable(){
        generalMatchTableView.setRoot(rootGeneralMatch);
        generateColumns();
        setRowFactoryOfTable();
        blockTableHozizontalScroll();

        loadAllEventMatches();
    }

    private void blockTableHozizontalScroll(){
        generalMatchTableView.addEventFilter(ScrollEvent.ANY, event -> {
            if (event.getDeltaX() != 0) event.consume();
        });
    }

    @FXML
    protected void downloadGeneralMatchTablePDF() throws IOException, DocumentException {
        File choosedDirectory = openDirectoryChooserAndReturnDirectory();

        PdfTableGenerator pdfTableGenerator = new PdfTableGenerator();
        int tabNumber = tabIndex+1;
        String title = strings.getString("generalMatchTable") + " " + tabNumber + " - " + modalityAndGender;
        String fileName = title + " - " + LocalDate.now() + ".pdf";

        pdfTableGenerator.generateGeneralMatchTablePdf(
                title, choosedDirectory + "/" + fileName, rootGeneralMatch.getChildren());
        HomeController.showToastMessage(strings.getString("successDownloadPDF"));
    }


    @FXML
    protected void changeTab(ActionEvent event){
        JFXToggleNode toggleNode = (JFXToggleNode) event.getSource();
        if(!toggleNode.isSelected()) toggleNode.setSelected(true);

        tabIndex = tabGroup.getToggles().indexOf(tabGroup.getSelectedToggle());
        loadAllEventMatches();
    }

    @FXML
    protected void handleBtnChangeMatchToAnotherTab(){
        if(!generalMatchTableView.getSelectionModel().isEmpty()) changeMatchToAnotherTab();
        else HomeController.showToastMessage(strings.getString("selectMatchFirst"));
    }

    private void changeMatchToAnotherTab(){
        int selectedIndex = generalMatchTableView.getSelectionModel().getSelectedIndex();
        GeneralMatch selectedMatch = rootGeneralMatch.getChildren().get(selectedIndex).getValue();

        event.getMatches().get(tabIndex).remove(selectedIndex);
        tabIndex = tabIndex == 0 ? 1 : 0;
        event.getMatches().get(tabIndex).add(selectedMatch);

        tabGroup.selectToggle(tabGroup.getToggles().get(tabIndex));
        loadAllEventMatches();
        generalMatchTableView.getSelectionModel().select(rootGeneralMatch.getChildren().size() - 1);

        new EventService().updateEventMatches(event.getId(), event.getMatches());
    }

    private void loadAllEventMatches(){
        rootGeneralMatch.getChildren().clear();
        event.getMatches().get(tabIndex).forEach(match -> rootGeneralMatch.getChildren().add(new TreeItem<>(match)));
    }

    private void generateColumns(){
        modalityColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getModality()));
        genderColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getModality() != null ?
                        strings.getString(param.getValue().getValue().getGender()) : ""));
        versusColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getValue().getModality() != null ? "X" : ""));
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
        generalMatchTableView.setRowFactory(tv -> {
            TreeTableRow<GeneralMatch> row = new TreeTableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    int index = row.getIndex();
                    draggedItem = rootGeneralMatch.getChildren().get(index);

                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    rootGeneralMatch.getChildren().remove(draggedItem);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                generalMatchTableView.getSelectionModel().clearSelection();

                rootGeneralMatch.getChildren().removeIf(item -> item.getValue().getModality() == null);

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    nextItems = FXCollections.observableArrayList();

                    for (int x = row.getIndex(); x < rootGeneralMatch.getChildren().size(); x++)
                        nextItems.add(rootGeneralMatch.getChildren().get(x));

                    resetMatchTableItens();
                    generalMatchTableView.getSelectionModel().select(row.getIndex());

                    rowIndex = row.getIndex();
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(e -> {
                Dragboard db = e.getDragboard();

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int dropIndex = row.isEmpty() ? rootGeneralMatch.getChildren().size() : row.getIndex();

                    if(dropIndex != rootGeneralMatch.getChildren().size()){
                        rootGeneralMatch.getChildren().get(rowIndex).setValue(draggedItem.getValue());
                        generalMatchTableView.getSelectionModel().select(dropIndex);
                    } else {
                        rootGeneralMatch.getChildren().add(draggedItem);
                        generalMatchTableView.getSelectionModel().select(rootGeneralMatch.getChildren().size()-1);
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
        int selectedIndex = generalMatchTableView.getSelectionModel().getSelectedIndex();
        boolean invalidDrop = event.getMatches().get(tabIndex).size() != rootGeneralMatch.getChildren().size() ||
                rootGeneralMatch.getChildren().stream().anyMatch(item -> item.getValue().getModality() == null);

        if(invalidDrop){
            loadAllEventMatches();
            generalMatchTableView.getSelectionModel().select(selectedIndex);
        } else
            updateEventMatches();
    }

    private void updateEventMatches(){
        event.getMatches().get(tabIndex).clear();
        rootGeneralMatch.getChildren().forEach(match -> {
            GeneralMatch generalMatch = match.getValue();
            event.getMatches().get(tabIndex).add(generalMatch);
        });

        new EventService().updateEventMatches(event.getId(), event.getMatches());
    }

    private void resetMatchTableItens(){
        rootGeneralMatch.getChildren().removeAll(nextItems);
        rootGeneralMatch.getChildren().add(new TreeItem<>(new GeneralMatch()));
        rootGeneralMatch.getChildren().addAll(nextItems);
    }
}

