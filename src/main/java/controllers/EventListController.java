package controllers;

import application.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Event;
import services.EventService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventListController {
    public GridPane gridEvents;
    public Label lblPageIndex;
    public AnchorPane panePageNav;

    private List<List<Event>> eventPages;
    private int numberOfPages;
    private int pageIndex;

    static List<Event> institutionEvents;
    static Event event;

    @FXML
    public void initialize() {
        EventService eventService = new EventService();
        List<String> eventsId = Session.getInstance().getInstitution().getEvents_id();
        institutionEvents = eventService.requestAllEventsOfInstitution(eventsId);
        numberOfPages = calculateNumberOfPages();
        pageIndex = 0;

        if(eventsId.size() > 0){
            paginationEvents();
            listEventsPage();
            panePageNav.setVisible(true);
        }
    }

    private void listEventsPage() {
        List<Event> eventListPage = eventPages.get(pageIndex);
        lblPageIndex.setText(String.valueOf(pageIndex + 1));
        gridEvents.getChildren().clear();

        for (int i = 0; i < eventListPage.size(); i++) {
            event = eventListPage.get(i);
            addNewEventItem(i);
        }
    }

    private void paginationEvents(){
        eventPages = new ArrayList<>();

        for(int pageIndex = 0; pageIndex < numberOfPages; pageIndex++)
            eventPages.add(appendEventsOnPageAndReturn(pageIndex));
    }

    private List<Event> appendEventsOnPageAndReturn(int pageIndex){
        List<Event> page = new ArrayList<>();
        boolean lastPage = pageIndex == numberOfPages-1;
        int eventIndex = 9 * pageIndex;
        int eventLimit = lastPage ? institutionEvents.size() - eventIndex : 9;

        for(int eventCount = 0; eventCount < eventLimit; eventCount++){
            page.add(institutionEvents.get(eventIndex));
            eventIndex++;
        }
        return page;
    }

    private int calculateNumberOfPages(){
        return (institutionEvents.size() / 9) + (institutionEvents.size() % 9 == 0 ? 0 : 1);
    }

    @FXML
    protected void navigatePreviousPage(){
        if(pageIndex > 0){
            pageIndex--;
            listEventsPage();
        }
    }

    @FXML
    protected void navigateNextPage(){
        if(pageIndex < eventPages.size()-1){
            pageIndex++;
            listEventsPage();
        }
    }

    private void addNewEventItem(int eventCount){
        try{
            AnchorPane eventItem = FXMLLoader.load(getClass().getResource("/views/partials/event-item.fxml"));
            gridEvents.add(eventItem, eventCount % 3,eventCount / 3);
        } catch (Exception e){ e.printStackTrace(); }
    }

    @FXML
    protected void openEventCreateView(){
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/event-create.fxml")));
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
}
