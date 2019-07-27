package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InitialController {
    public JFXTextField txtRegisterName;
    public JFXTextField txtRegisterEmail;
    public JFXPasswordField txtRegisterPassword;
    public JFXTextField txtLoginEmail;
    public JFXPasswordField txtLoginPassword;
    public Label lblErrorMessage;

    @FXML
    public void initialize() {
    }

    @FXML
    protected void efetuateInstitutionLogin() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        boolean validLogin = institutionAuth.login(txtLoginEmail.getText(), txtLoginPassword.getText());

        if(!validLogin)
            lblErrorMessage.setText("Falha ao efetuar login");
    }
}
