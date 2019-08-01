package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Institution;
import services.InstitutionService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InitialController implements Initializable {
    public JFXTextField txtRegisterName;
    public JFXTextField txtRegisterEmail;
    public JFXPasswordField txtRegisterPassword;
    public JFXTextField txtLoginEmail;
    public JFXPasswordField txtLoginPassword;
    public Label lblLoginMessage;
    public Label lblRegisterMessage;
    public JFXButton btnLogin;

    private InstitutionService institutionService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        institutionService = new InstitutionService();
    }

    @FXML
    protected void efetuateInstitutionLogin() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        boolean validLogin = institutionAuth.login(txtLoginEmail.getText(), txtLoginPassword.getText());

        if(!validLogin)
            lblLoginMessage.setText("Falha ao efetuar login");
        else
            switchScene();
    }

    @FXML
    protected void efetuateInstitutionRegister() {
        Institution newInstitution = new Institution(txtRegisterName.getText(), txtRegisterEmail.getText(),
                txtRegisterPassword.getText());
        boolean validRegister = institutionService.insertInstitution(newInstitution);

        lblRegisterMessage.setText(validRegister ? "Cadastro efetuado com sucesso" : "Falha ao efetuar o cadastro");
    }

    private void switchScene(){
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene;

        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/home.fxml")));

            stage.setScene(scene);
            stage.setResizable(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
