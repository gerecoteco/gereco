package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import models.Modality;

import java.net.URL;
import java.util.List;

public class EventItemController {
    public GridPane gridModalities;
    public Label lblEventName;
    public Label lblModalities;

    static String eventName;

    @FXML
    public void initialize() {
        lblEventName.setText(EventListController.event.getName());
        listAllModalities();
    }

    @FXML
    protected void openEventPageView(){
        eventName = lblEventName.getText();
        URL eventPageURL = getClass().getResource("/views/event-page.fxml");
        HomeController.loadView(eventPageURL);
    }

    @FXML
    protected void loadDeleteDialog(){
        eventName = lblEventName.getText();
        JFXDialogLayout content = new JFXDialogLayout();
        HBox hbox = new HBox(5);
        JFXDialog dialog;

        JFXButton btnConfirm = new JFXButton("Sim");
        JFXButton btnCancel = new JFXButton("Cancelar");

        content.setHeading(new Label("Excluir evento"));
        content.setBody(new Text("Tem certeza que deseja excluir o evento " + eventName + "?"));

        hbox.getChildren().add(btnCancel);
        hbox.getChildren().add(btnConfirm);
        content.setActions(hbox);

        btnCancel.setStyle("-fx-background-color: #369137");
        btnConfirm.setStyle("-fx-background-color: #369137");

        dialog = new JFXDialog(HomeController.staticStackPaneMain, content, JFXDialog.DialogTransition.CENTER);

        btnCancel.setOnAction(action -> dialog.close());
        dialog.show();
    }

    private void listAllModalities() {
        List<Modality> modalities = EventListController.event.getModalities();
        StringBuilder modalitiesString = new StringBuilder();

        modalities.forEach(modality ->  modalitiesString.append(modality.getName()).append(" | "));
        lblModalities.setText(modalitiesString.toString().substring(0, modalitiesString.length()-2));
    }
}
