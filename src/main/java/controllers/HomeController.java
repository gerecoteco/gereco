package controllers;

import application.Session;
import com.google.gson.Gson;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import models.Event;
import models.Institution;
import services.EventService;

import java.util.List;

public class HomeController {
    public Label lblInstitutionInfo;
    public GridPane gridEvents;

    private Institution institutionLogged;
    private EventService eventService;
    static Event event;

    @FXML
    public void initialize() {
        efetuateInstitutionAuth();

        eventService = new EventService();
        institutionLogged = Session.getInstance().getInstitution();

        lblInstitutionInfo.setText(institutionLogged.getName() + " |  " + institutionLogged.getEmail());
        listAllEvents();
    }

    private void efetuateInstitutionAuth() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        System.out.println(institutionAuth.login("etec@etec.com", "12345"));
    }

    private void listAllEvents() {
        Gson gson = new Gson();
        List<String> eventsId = institutionLogged.getEvents_id();

        int columnSize = 2;
        for (int i = 0; i < eventsId.size(); i++) {
            event = gson.fromJson(eventService.requestOneEvent(eventsId.get(i)), Event.class);
            try{
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/views/partials/event.fxml"));
                anchorPane.getStyleClass().add("event");
                gridEvents.add(anchorPane, i % columnSize,i / columnSize);
            } catch (Exception e){ e.printStackTrace(); }
        }
    }
}
