package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import models.Modality;

import java.util.List;

public class EventItemController {
    public GridPane gridModalities;
    public Label lblEventName;
    public Label lblModalities;

    @FXML
    public void initialize() {
        lblEventName.setText(HomeController.event.getName());
        listAllModalities();
    }

    private void listAllModalities() {
        List<Modality> modalities = HomeController.event.getModalities();
        StringBuilder modalitiesString = new StringBuilder();
        for (Modality modality : modalities) {
            try {
                modalitiesString.append(modality.getName()).append(" | ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        lblModalities.setText(modalitiesString.toString().substring(0, modalitiesString.length()-2));
    }
}
