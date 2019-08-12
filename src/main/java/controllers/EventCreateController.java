package controllers;

import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class EventCreateController {
    public JFXChipView chipModalities;
    public JFXComboBox cbxModalities;

    @FXML
    public void initialize() {

    }

    @FXML
    protected void appendModalitiesOnComboBox(KeyEvent e){
        if(e.getCode() == KeyCode.ENTER)
            cbxModalities.setItems(chipModalities.getChips());
    }

    @FXML
    protected void cancelEventCreation(ActionEvent event){
        Button btnCancel = (Button) event.getSource();
        Stage actualStage = (Stage) btnCancel.getScene().getWindow();
        actualStage.close();
    }
}
