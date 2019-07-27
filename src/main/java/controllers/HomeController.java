package controllers;

import application.Session;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import models.Institution;

import java.io.IOException;

public class HomeController {
    public Label lblInstitutionInfo;
    public ScrollPane scrollHome;

    @FXML
    public void initialize() {
        efetuateInstitutionAuth();

        Institution institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionInfo.setText(institutionLogged.getName() + " |  " + institutionLogged.getEmail());

        loadView();
    }

    private void efetuateInstitutionAuth() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        System.out.println(institutionAuth.login("etec@etec.com", "12345"));
    }

    private void loadView(){
        try{
            scrollHome.setContent(FXMLLoader.load(getClass().getResource("/views/event-list.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
