package controllers.home.event_list;

import com.jfoenix.controls.JFXDialog;
import controllers.home.HomeController;
import helpers.DialogBuilder;
import helpers.UTF8Control;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Modality;
import services.EventService;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.external.PasswordValidationController.deleteInstitution;

public class EventItemController implements Initializable {
    public Label lblEventName;
    public Label lblModalities;
    public Label lblEventId;

    private static String successEventDelete;
    public static String eventId;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        successEventDelete = strings.getString("successEventDelete");

        lblEventId.setText(EventListController.event.getId());
        lblEventName.setText(EventListController.event.getName());
        listAllModalities();
    }

    @FXML
    protected void openEventPageView(){
        eventId = lblEventId.getText();
        HomeController.loadEventPageView();
    }

    @FXML
    protected void loadDeleteDialog(){
        eventId = lblEventId.getText();
        deleteInstitution = false;

        String heading = strings.getString("deleteEventDialog.heading");
        String body = MessageFormat.format(strings.getString("deleteEventDialog.body"), lblEventName.getText());
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
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/views/external-forms/password-validation.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            Scene scene = new Scene(root);

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

    public static void deleteEvent(){
        EventService eventService = new EventService();
        eventService.deleteEvent(eventId);
        HomeController.loadEventListView();
        HomeController.showToastMessage(successEventDelete);
    }

    private void listAllModalities() {
        List<Modality> modalities = EventListController.event.getModalities();
        StringBuilder modalitiesString = new StringBuilder();

        modalities.forEach(modality ->  modalitiesString.append(modality.getName()).append(" | "));
        lblModalities.setText(modalitiesString.toString().substring(0, modalitiesString.length()-2));
    }
}
