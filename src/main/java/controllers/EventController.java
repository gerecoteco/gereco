package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.Modality;

import java.util.List;

public class EventController {
    public GridPane gridModalities;
    public Label lblEventName;

    @FXML
    public void initialize() {
        lblEventName.setText(HomeController.event.getName());
        listAllEvents();
    }

    private void listAllEvents() {
        List<Modality> modalities = HomeController.event.getModalities();
        for (int x = 0; x < modalities.size(); x++) {
            try{
                gridModalities.addRow(x, new Label(modalities.get(x).getName()));
            } catch (Exception e){ e.printStackTrace(); }
        }
    }
}
