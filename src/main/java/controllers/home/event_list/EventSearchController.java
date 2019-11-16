package controllers.home.event_list;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class EventSearchController implements Initializable {
    public JFXTextField txtEventName;
    public JFXDatePicker dateCreateDate1;
    public JFXDatePicker dateCreateDate2;
    public JFXComboBox cbxEventStatus;
    public JFXButton btnSearch;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
    }
}
