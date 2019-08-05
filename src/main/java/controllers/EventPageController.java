package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventPageController {
    public Label lblEventName;
    public JFXComboBox cbxModalities;

    private Event event;

    @FXML
    public void initialize() {
        event = findEventByName();
        lblEventName.setText(event.getName());

        appendModalitiesInComboBox();
    }

    private Event findEventByName(){
        return EventListController.institutionEvents.stream().filter(
                event -> event.getName().equals(EventItemController.eventName)).findFirst().orElse(null);
    }

    private void appendModalitiesInComboBox(){
        List<String> modalities = new ArrayList<>();
        event.getModalities().forEach(modality -> modalities.add(modality.getName()));

        cbxModalities.setItems(FXCollections.observableList(modalities));
    }
}
