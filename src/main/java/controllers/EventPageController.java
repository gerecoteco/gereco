package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Event;

import java.io.IOException;
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

    @FXML
    protected void openMatchFormView(){
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/match-form.fxml")));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
