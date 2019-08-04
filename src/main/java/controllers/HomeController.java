package controllers;

import application.Main;
import application.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import models.Institution;

import java.io.IOException;

public class HomeController {
    public Label lblInstitutionInfo;
    public ScrollPane scrollHome;

    @FXML
    public void initialize() {
        Institution institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionInfo.setText(institutionLogged.getName() + " |  " + institutionLogged.getEmail());

        loadView();
    }

    @FXML
    protected void logout(){
        Session.getInstance().setInstitution(null);
        Main.mainStage.close();
        Main.mainStage = new Stage();

        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/initial.fxml")));

            Main.mainStage .setScene(scene);
            Main.mainStage .setResizable(false);
            Main.mainStage .centerOnScreen();
            Main.mainStage .show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(){
        try{
            scrollHome.setContent(FXMLLoader.load(getClass().getResource("/views/event-list.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
