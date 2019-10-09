package controllers;

import application.Session;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleNode;
import helpers.UTF8Control;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Event;
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EventListController implements Initializable {
    public GridPane gridEvents;
    public GridPane gridNavigation;
    public JFXComboBox cbxEventsPerPage;
    public HBox paneNavigation;
    public HBox hboxEventList;
    public HBox hboxEventsPerPage;

    private List<List<Event>> eventPages;
    private ToggleGroup navigationButtons;
    private int eventsPerPage;
    private int numberOfPages;
    private int pageIndex;

    private ResourceBundle strings;
    static List<Event> institutionEvents;
    static Event event;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        List<String> eventsId = Session.getInstance().getInstitution().getEvents_id();
        generateCbxEventsPerPageItems();

        institutionEvents = new EventService().requestAllEventsOfInstitution(eventsId);
        navigationButtons = new ToggleGroup();
        eventsPerPage = Integer.parseInt(cbxEventsPerPage.getValue().toString());
        numberOfPages = getNumberOfPages();
        pageIndex = 0;

        if(eventsId.size() > 0) showEventsOnView();
        else showNoEventsMessage();
    }

    private void generateCbxEventsPerPageItems(){
        Object[] EVENTS_PER_PAGE_OPTIONS = {6, 9, 12, 15, strings.getString("eventsPerPage.all")};
        cbxEventsPerPage.getItems().addAll(EVENTS_PER_PAGE_OPTIONS);
        cbxEventsPerPage.setValue(9);
    }

    private void showNoEventsMessage(){
        hboxEventsPerPage.setVisible(false);
        paneNavigation.setVisible(false);

        Label message = new Label(strings.getString("noEvent"));
        message.getStyleClass().add("no-events-message");
        hboxEventList.getChildren().clear();
        hboxEventList.getChildren().add(message);
    }

    private void showEventsOnView(){
        paginationEvents();
        listEventsPage();
    }

    @FXML
    public void changeNumberOfEventsPerPage(){
        eventsPerPage = cbxEventsPerPage.getValue().equals(strings.getString("eventsPerPage.all")) ?
                institutionEvents.size() : Integer.parseInt(cbxEventsPerPage.getValue().toString());
        numberOfPages = getNumberOfPages();
        pageIndex = 0;

        navigationButtons.getToggles().clear();
        gridNavigation.getChildren().clear();
        showEventsOnView();
    }

    private void listEventsPage() {
        List<Event> eventListPage = eventPages.get(pageIndex);
        gridEvents.getChildren().clear();

        for (int i = 0; i < eventListPage.size(); i++) {
            event = eventListPage.get(i);
            loadNewEventItem(i);
        }
    }

    private void paginationEvents(){
        eventPages = new ArrayList<>();
        boolean haveOnlyOnePage = numberOfPages == 1;

        if(haveOnlyOnePage)
            generateOnePageNavigation();
        else
            generateMultiplePageNavigation();
    }

    private void generateOnePageNavigation(){
        eventPages.add(appendEventsOnPageAndReturn(0));
        paneNavigation.setVisible(false);
    }

    private void generateMultiplePageNavigation(){
        for(int pageIndex = 0; pageIndex < numberOfPages; pageIndex++){
            eventPages.add(appendEventsOnPageAndReturn(pageIndex));
            generateNavigationButton(pageIndex);
        }
        navigationButtons.getToggles().get(0).setSelected(true);
        paneNavigation.setVisible(true);
    }

    private void generateNavigationButton(int pageIndex){
        JFXToggleNode toggleNode = new JFXToggleNode();
        toggleNode.setText(String.valueOf(pageIndex + 1));
        toggleNode.setOnAction(this::navigateThroughThePages);
        toggleNode.setToggleGroup(navigationButtons);
        gridNavigation.add(toggleNode, pageIndex, 0);
    }

    private void navigateThroughThePages(ActionEvent event){
        JFXToggleNode toggleNode = (JFXToggleNode) event.getSource();
        if(!toggleNode.isSelected()) toggleNode.setSelected(true);

        pageIndex = Integer.parseInt(toggleNode.getText()) - 1;
        listEventsPage();
    }

    private List<Event> appendEventsOnPageAndReturn(int pageIndex){
        List<Event> page = new ArrayList<>();
        boolean isLastPage = pageIndex == numberOfPages-1;
        int eventIndex = eventsPerPage * pageIndex;
        int eventLimit = isLastPage ? institutionEvents.size() - eventIndex : eventsPerPage;

        for(int eventCount = 0; eventCount < eventLimit; eventCount++){
            page.add(institutionEvents.get(eventIndex));
            eventIndex++;
        }
        return page;
    }

    private int getNumberOfPages(){
        return (institutionEvents.size() / eventsPerPage) + (institutionEvents.size() % eventsPerPage == 0 ? 0 : 1);
    }

    @FXML
    protected void navigateToPreviousPage(){
        if(pageIndex > 0){
            pageIndex--;
            navigationButtons.getToggles().get(pageIndex).setSelected(true);
            listEventsPage();
        }
    }

    @FXML
    protected void navigateToNextPage(){
        if(pageIndex < eventPages.size()-1){
            pageIndex++;
            navigationButtons.getToggles().get(pageIndex).setSelected(true);
            listEventsPage();
        }
    }

    private void loadNewEventItem(int eventCount){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/partials/event-item.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));

            AnchorPane eventItem = (AnchorPane) root;
            gridEvents.add(eventItem, eventCount % 3,eventCount / 3);
        } catch (Exception e){ e.printStackTrace(); }
    }

    @FXML
    protected void openEventCreateView(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/views/external-forms/event-create.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            Scene scene = new Scene(root);

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
