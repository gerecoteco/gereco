package controllers;

import application.Session;
import com.google.gson.Gson;
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

    private Institution institutionLogged;
    private EventService eventService;
    private List<String> eventsId;

    @FXML
    public void initialize() {
        eventService = new EventService();
        institutionLogged = Session.getInstance().getInstitution();
        eventsId = institutionLogged.getEvents_id();

        listAllEvents();
    }

    private void listAllEvents() {
        Gson gson = new Gson();

        int columnSize = 3;

        for (int i = 0; i < 9; i++) {
            HomeController.event = gson.fromJson(eventService.requestOneEvent(eventsId.get(i)), Event.class);
            try{
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/views/partials/event-item.fxml"));
                anchorPane.getStyleClass().add("event");
                gridEvents.add(anchorPane, i % columnSize,i / columnSize);
            } catch (Exception e){ e.printStackTrace(); }
        }
    }
}
