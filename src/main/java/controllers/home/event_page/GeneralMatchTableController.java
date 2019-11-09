package controllers.home.event_page;

import com.jfoenix.controls.JFXTreeTableView;
import helpers.MatchTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.net.URL;
import java.util.ResourceBundle;

import static controllers.home.event_page.EventPageController.modalityAndGender;

public class GeneralMatchTableController implements Initializable {
    public JFXTreeTableView matchTableView;
    public TreeTableColumn<MatchTableModel, String> modalityColumn, genderColumn,versusColumn, teamAColumn, teamBColumn;
    public TreeTableColumn<MatchTableModel, Number> stageColumn;
    public Label lblModalityAndGender;
    private TreeItem<MatchTableModel> rootMatch = new TreeItem<>(new MatchTableModel());

    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private int rowIndex = 0;
    private TreeItem<MatchTableModel> draggedItem;
    private ObservableList<TreeItem<MatchTableModel>> nextItems;

    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        lblModalityAndGender.setText(modalityAndGender);

        matchTableView.setRoot(rootMatch);
        generateColumns();
        setRowFactoryOfTable();
    }

    private void generateColumns(){
        modalityColumn.setCellValueFactory(param -> param.getValue().getValue().modalityProperty());
        genderColumn.setCellValueFactory(param -> param.getValue().getValue().genderProperty());
        stageColumn.setCellValueFactory(param -> param.getValue().getValue().stageProperty());
        versusColumn.setCellValueFactory(param -> param.getValue().getValue().versusProperty());
        teamAColumn.setCellValueFactory(param -> param.getValue().getValue().teamAProperty());
        teamBColumn.setCellValueFactory(param -> param.getValue().getValue().teamBProperty());
    }

    private void setRowFactoryOfTable(){
        matchTableView.setRowFactory(tv -> {
            TreeTableRow<MatchTableModel> row = new TreeTableRow<>();

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

                rootMatch.getChildren().removeIf(item -> item.getValue().stageProperty() == null);

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    nextItems = FXCollections.observableArrayList();

                    for (int x = row.getIndex(); x < rootMatch.getChildren().size(); x++)
                        nextItems.add(rootMatch.getChildren().get(x));

                    resetMatchTableItens();

                    rowIndex = row.getIndex();
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    event.consume();
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int dropIndex = row.isEmpty() ? rootMatch.getChildren().size() : row.getIndex();

                    if(dropIndex != rootMatch.getChildren().size()){
                        rootMatch.getChildren().get(rowIndex).setValue(draggedItem.getValue());
                        matchTableView.getSelectionModel().select(dropIndex);
                    } else {
                        rootMatch.getChildren().add(draggedItem);
                        matchTableView.getSelectionModel().select(rootMatch.getChildren().size()-1);
                    }

                    event.setDropCompleted(true);
                    event.consume();
                }
            });
            return row ;
        });
    }

    private void resetMatchTableItens(){
        rootMatch.getChildren().removeAll(nextItems);
        rootMatch.getChildren().add(new TreeItem<>(new MatchTableModel()));
        rootMatch.getChildren().addAll(nextItems);
    }
}

