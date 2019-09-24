package controllers;

import application.Main;
import application.Session;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Institution;

import java.io.IOException;
import java.net.URL;

public class HomeController {
    public Label lblInstitutionInfo;
    public ScrollPane scrollHome;
    public StackPane stackPaneMain;

    static StackPane staticStackPaneMain;
    private static Label staticLblInstitutionInfo;
    private URL eventListURL;

    @FXML
    public void initialize() {
        eventListURL = getClass().getResource("/views/home/event-list.fxml");

        staticStackPaneMain = stackPaneMain;
        staticLblInstitutionInfo = lblInstitutionInfo;

        loadInstitutionInfoOnLabel();
        loadView(eventListURL);
    }

    static void loadInstitutionInfoOnLabel(){
        Institution institutionLogged = Session.getInstance().getInstitution();
        staticLblInstitutionInfo.setText(institutionLogged.getName() + " |  " + institutionLogged.getEmail());
    }

    @FXML
    protected void loadHome(){
        loadView(eventListURL);
    }

    @FXML
    protected void openConfigView(){
        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/external-forms/institution-config.fxml")));
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

    @FXML
    protected void logout(){
       returnToInitialView();
    }

    static void returnToInitialView(){
        Session.getInstance().setInstitution(null);
        Main.mainStage.close();
        Main.mainStage = new Stage();

        try {
            Scene scene = new Scene(FXMLLoader.load(HomeController.class.getResource("/views/initial.fxml")));

            Main.mainStage.setScene(scene);
            Main.mainStage.setResizable(false);
            Main.mainStage.centerOnScreen();
            Main.mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void loadView(URL viewURL){
        try{
            staticStackPaneMain.getChildren().clear();
            staticStackPaneMain.getChildren().add(FXMLLoader.load(viewURL));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void showToastMessage(String messsage) {
        JFXSnackbar snackbar = new JFXSnackbar(staticStackPaneMain);
        snackbar.getStylesheets().add(HomeController.class.getResource("/css/snackbar.css").toString());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(messsage, "OK", action -> snackbar.close()),
                Duration.millis(3000), null));
    }
}
