package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Institution;
import services.InstitutionService;

public class InitialController {
    public JFXTextField txtRegisterName;
    public JFXTextField txtRegisterEmail;
    public JFXPasswordField txtRegisterPassword;
    public JFXTextField txtLoginEmail;
    public JFXPasswordField txtLoginPassword;
    public Label lblLoginMessage;
    public Label lblRegisterMessage;

    private InstitutionService institutionService;

    @FXML
    public void initialize() {
        institutionService = new InstitutionService();
    }

    @FXML
    protected void efetuateInstitutionLogin() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        boolean validLogin = institutionAuth.login(txtLoginEmail.getText(), txtLoginPassword.getText());

        if(!validLogin)
            lblLoginMessage.setText("Falha ao efetuar login");
    }

    @FXML
    protected void efetuateInstitutionRegister() {
        Institution newInstitution = new Institution(txtRegisterName.getText(), txtRegisterEmail.getText(),
                txtRegisterPassword.getText(),null);
        boolean validRegister = institutionService.insertInstitution(newInstitution);

        lblRegisterMessage.setText(validRegister ? "Cadastro efetuado com sucesso" : "Falha ao efetuar o cadastro");
    }
}
