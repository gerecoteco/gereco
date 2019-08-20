package controllers;

import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EventCreateController {
    public JFXChipView chipModalities;
    public JFXComboBox cbxModalities;

    @FXML
    public void initialize() {
        cbxModalities.setItems(chipModalities.getChips());
    }

    @FXML
    protected void cancelEventCreation(ActionEvent event){
        Button btnCancel = (Button) event.getSource();
        Stage actualStage = (Stage) btnCancel.getScene().getWindow();
        actualStage.close();
    }
}
