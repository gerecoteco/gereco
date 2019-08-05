package controllers;

import application.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Institution;

public class ConfigController {
    public Label lblInstitutionName;

    @FXML
    public void initialize() {
        Institution institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionName.setText(institutionLogged.getName());
    }
}
