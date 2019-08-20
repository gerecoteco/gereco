package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import helpers.DialogBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Modality;

import java.net.URL;
import java.util.List;

public class EventItemController {
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
        URL eventPageURL = getClass().getResource("/views/home/event-page.fxml");
        HomeController.loadView(eventPageURL);
    }

    @FXML
    protected void loadDeleteDialog(){
        eventName = lblEventName.getText();
        JFXButton btnConfirm = new JFXButton("Sim");
        String heading = "Excluir evento";
        String body = "Tem certeza que deseja excluir o evento " + eventName + "?";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, btnConfirm, HomeController.staticStackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        btnConfirm.setOnAction(action -> dialog.close());
        dialog.show();
    }

    private void listAllModalities() {
        List<Modality> modalities = EventListController.event.getModalities();
        StringBuilder modalitiesString = new StringBuilder();

        modalities.forEach(modality ->  modalitiesString.append(modality.getName()).append(" | "));
        lblModalities.setText(modalitiesString.toString().substring(0, modalitiesString.length()-2));
    }
}
