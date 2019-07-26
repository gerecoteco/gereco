package controllers;

import application.Session;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import models.Event;
import models.Institution;

import java.io.IOException;

public class HomeController {
    public Label lblInstitutionInfo;
    public ScrollPane scrollHome;

    private Institution institutionLogged;
    static Event event;

    @FXML
    public void initialize() {
        efetuateInstitutionAuth();

        institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionInfo.setText(institutionLogged.getName() + " |  " + institutionLogged.getEmail());

        loadView();
    }

    private void efetuateInstitutionAuth() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        System.out.println(institutionAuth.login("etec@etec.com", "12345"));
    }

    private void loadView(){
        try{
            HBox hBox = FXMLLoader.load(getClass().getResource("/views/event-list.fxml"));
            scrollHome.setContent(hBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
