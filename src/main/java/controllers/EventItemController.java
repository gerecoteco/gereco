package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.Event;
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

    private void listAllModalities() {
        List<Modality> modalities = EventListController.event.getModalities();
        StringBuilder modalitiesString = new StringBuilder();

        modalities.forEach(modality ->  modalitiesString.append(modality.getName()).append(" | "));
        lblModalities.setText(modalitiesString.toString().substring(0, modalitiesString.length()-2));
    }
}
