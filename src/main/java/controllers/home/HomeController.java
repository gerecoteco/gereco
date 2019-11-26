package controllers.home;

import application.Main;
import application.Session;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import controllers.home.event_list.EventSearchController;
import controllers.home.event_page.EventPageController;
import controllers.home.event_page.GroupTableController;
import helpers.UTF8Control;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Institution;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static application.Main.mainStage;

public class HomeController {
    public Label lblInstitutionInfo;
    public ScrollPane scrollHome;
    public StackPane stackPaneMain;

    public static StackPane staticStackPaneMain;
    private static Label staticLblInstitutionInfo;

    @FXML
    public void initialize() {
        staticStackPaneMain = stackPaneMain;
        staticLblInstitutionInfo = lblInstitutionInfo;

        loadInstitutionInfoOnLabel();
        loadEventListView();
    }

    public static void loadInstitutionInfoOnLabel(){
        Institution institutionLogged = Session.getInstance().getInstitution();
        staticLblInstitutionInfo.setText(institutionLogged.getName() + " |  " + institutionLogged.getEmail());
    }

    @FXML
    protected void loadHome(){
        EventSearchController.resetFilters();
        loadEventListView();
    }

    @FXML
    protected void openConfigView(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/external/institution-config.fxml"),
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

    @FXML
    protected void logout(){
       returnToInitialView();
    }

    public static void returnToInitialView(){
        Session.getInstance().setInstitution(null);
        Main.mainStage.close();
        Main.mainStage = new Stage();

        try {
            Parent root = FXMLLoader.load(HomeController.class.getResource("/views/initial.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            Scene scene = new Scene(root);

            Main.mainStage.setScene(scene);
            Main.mainStage.setResizable(false);
            Main.mainStage.centerOnScreen();
            Main.mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadEventListView(){
        final URL EVENT_LIST_URL = HomeController.class.getResource("/views/home/event_list/event-list.fxml");
        EventPageController.actualGender = null;
        GroupTableController.groupIndex = 0;
        loadView(EVENT_LIST_URL);
    }

    public static void loadEventPageView(){
        final URL EVENT_PAGE_URL = HomeController.class.getResource("/views/home/event_page/event-page.fxml");
        loadView(EVENT_PAGE_URL);
    }

    private static void loadView(URL viewURL){
        try{
            Parent root = FXMLLoader.load(viewURL,
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));

            staticStackPaneMain.getChildren().clear();
            staticStackPaneMain.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File openDirectoryChooserAndReturnDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(mainStage);
    }

    public static void showToastMessage(String messsage) {
        JFXSnackbar snackbar = new JFXSnackbar(staticStackPaneMain);
        snackbar.getStylesheets().add(HomeController.class.getResource("/css/external/snackbar.css").toString());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(messsage, "OK", action -> snackbar.close()),
                Duration.millis(3000), null));
    }
}
