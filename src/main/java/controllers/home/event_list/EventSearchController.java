package controllers.home.event_list;

import application.Session;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import controllers.home.HomeController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import models.Event;
import models.EventStatus;
import services.EventService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EventSearchController implements Initializable {
    public JFXTextField txtEventName;
    public JFXDatePicker dateCreateDate1;
    public JFXDatePicker dateCreateDate2;
    public JFXComboBox cbxEventStatus;
    public JFXButton btnSearch;
    private ResourceBundle strings;

    public static List<Event> filteredEvents;
    public static String eventName;
    public static String eventStatus;
    public static LocalDate date1;
    public static LocalDate date2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        final Object[] EVENT_STATUS_ITEMS = {EventStatus.PLANNING.getText(), EventStatus.IN_PROGRESS.getText(),
                EventStatus.FINISHED.getText(), EventStatus.CANCELED.getText(), strings.getString("all")};
        cbxEventStatus.getItems().addAll(EVENT_STATUS_ITEMS);

        txtEventName.setText(eventName);
        dateCreateDate1.setValue(date1);
        dateCreateDate2.setValue(date2);
        cbxEventStatus.setValue(eventStatus == null ? strings.getString("all") : eventStatus);
    }

    @FXML
    protected void searchEvent(){
        eventName = txtEventName.getText();
        eventStatus = cbxEventStatus.getValue().toString();
        date1 = dateCreateDate1.getValue();
        date2 = dateCreateDate2.getValue();

        filteredEvents = new EventService()
                .requestAllEventsOfInstitution(Session.getInstance().getInstitution().getEvents_id());

        filterEvents();
        if(!filteredEvents.isEmpty())
            HomeController.loadEventListView();
        else
            HomeController.showToastMessage(strings.getString("searchEvent.noEventsFound"));
    }

    private void filterEvents(){
        if(eventName != null) {
            filteredEvents = filteredEvents.stream().filter(event ->
                    event.getName().toLowerCase().contains(eventName.toLowerCase())).collect(Collectors.toList());
        } if(date1 != null && date2 != null){
            filteredEvents = filteredEvents.stream().filter(event ->
                    event.getDate().isAfter(date1) && event.getDate().isBefore(date2)).collect(Collectors.toList());
        } if(!eventStatus.equals(strings.getString("all"))){
            filteredEvents = filteredEvents.stream().filter(event ->
                    event.getEventStatus().getText().equals(eventStatus)).collect(Collectors.toList());
        }
    }
}
