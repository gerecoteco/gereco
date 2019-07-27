package controllers;

import application.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.Event;
import models.Institution;
import services.EventService;

import java.util.List;

public class EventListController {
    public GridPane gridEvents;

    private EventService eventService;
    private List<String> eventsId;
    static Event event;

    @FXML
    public void initialize() {
        eventService = new EventService();
        eventsId = Session.getInstance().getInstitution().getEvents_id();

        listAllEvents();
    }

    private void listAllEvents() {
        List<Event> institutionEvents = eventService.requestAllEventsOfInstitution(eventsId);

        for (int i = 0; i < 9; i++) {
            event = institutionEvents.get(i);
            addNewEventItem(i);
        }
    }

    private void addNewEventItem(int eventCount){
        try{
            AnchorPane eventItem = FXMLLoader.load(getClass().getResource("/views/partials/event-item.fxml"));
            gridEvents.add(eventItem, eventCount % 3,eventCount / 3);
        } catch (Exception e){ e.printStackTrace(); }
    }
}
