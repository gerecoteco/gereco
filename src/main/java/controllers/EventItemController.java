package controllers;

import com.jfoenix.controls.JFXDialog;
import helpers.DialogBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Modality;
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static controllers.PasswordValidationController.deleteInstitution;

public class EventItemController {
    public Label lblEventName;
    public Label lblModalities;
    public Label lblEventId;

    static String eventId;

    @FXML
    public void initialize() {
        lblEventId.setText(EventListController.event.getId());
        lblEventName.setText(EventListController.event.getName());
        listAllModalities();
    }

    @FXML
    protected void openEventPageView(){
        eventId = lblEventId.getText();
        URL eventPageURL = getClass().getResource("/views/home/event-page.fxml");
        HomeController.loadView(eventPageURL);
    }

    @FXML
    protected void loadDeleteDialog(){
        eventId = lblEventId.getText();
        deleteInstitution = false;

        String heading = "Excluir evento";
        String body = "Tem certeza que deseja excluir o evento " + lblEventName.getText() + "?";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, HomeController.staticStackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            loadPasswordValidationView();
            dialog.close();
        });
        dialog.show();
    }

    private void loadPasswordValidationView(){
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(
                    "/views/external-forms/password-validation.fxml")));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void deleteEvent(){
        EventService eventService = new EventService();
        eventService.deleteEvent(eventId);
        HomeController.loadView(EventItemController.class.getResource("/views/home/event-list.fxml"));
        HomeController.showToastMessage("Evento excluído com sucesso!");
    }

    private void listAllModalities() {
        List<Modality> modalities = EventListController.event.getModalities();
        StringBuilder modalitiesString = new StringBuilder();

        modalities.forEach(modality ->  modalitiesString.append(modality.getName()).append(" | "));
        lblModalities.setText(modalitiesString.toString().substring(0, modalitiesString.length()-2));
    }
}
