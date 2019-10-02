package controllers;

import application.Main;
import com.jfoenix.controls.*;
import helpers.DialogBuilder;
import helpers.InstitutionAuth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    public JFXButton btnLogin;
    public StackPane stackPaneLogin;

    private InstitutionService institutionService;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        institutionService = new InstitutionService();
    }

    @FXML
    protected void efetuateInstitutionLogin() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        boolean validLogin = institutionAuth.login(txtLoginEmail.getText(), txtLoginPassword.getText());

        if(!validLogin)
            showToastMessage("Falha ao efetuar login");
        else
            openHomeView();
    }

    @FXML
    protected void efetuateInstitutionRegister() {
        Institution newInstitution = new Institution(txtRegisterName.getText(), txtRegisterEmail.getText(),
                txtRegisterPassword.getText());

        String warning = InstitutionAuth.validatePasswordAndReturnMessage(
                txtRegisterPassword.getText(), txtRegisterPassword.getText());
        warning = txtRegisterName.getText().length() < 3 ? "O nome deve conter ao menos 3 caracteres" : warning;

        if(warning == null){
            boolean validRegister = institutionService.insertInstitution(newInstitution);
            showToastMessage(validRegister ? "Cadastro efetuado com sucesso" : "Falha ao efetuar o cadastro");
        } else
            showToastMessage(warning);
    }

    private void openHomeView(){
        Main.mainStage.close();
        Main.mainStage = new Stage();
        maximizeView();

        try {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/home/home.fxml")));

            Main.mainStage.setScene(scene);
            Main.mainStage.setResizable(true);
            Main.mainStage.centerOnScreen();
            Main.mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void maximizeView(){
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Main.mainStage.setX(bounds.getMinX());
        Main.mainStage.setY(bounds.getMinY());
        Main.mainStage.setWidth(bounds.getWidth());
        Main.mainStage.setHeight(bounds.getHeight());
    }

    @FXML
    protected void loadForgotPasswordDialog(){
        String heading = "Redefinir senha";
        String body = "Siga as instruções enviadas no seu email para redefinir sua senha";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, stackPaneLogin);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> dialog.close());
        dialog.show();
    }

    private void showToastMessage(String messsage) {
        JFXSnackbar snackbar = new JFXSnackbar(stackPaneLogin);
        snackbar.getStylesheets().add(getClass().getResource("/css/snackbar.css").toString());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(messsage, "OK", action -> snackbar.close()),
                Duration.millis(3000), null));
    }
}
