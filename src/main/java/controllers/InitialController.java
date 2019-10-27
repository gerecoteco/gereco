package controllers;

import application.Main;
import com.jfoenix.controls.*;
import helpers.DialogBuilder;
import helpers.InstitutionAuth;
import helpers.LocaleHelper;
import helpers.UTF8Control;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Institution;
import services.InstitutionService;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class InitialController implements Initializable {
    public JFXTextField txtRegisterName;
    public JFXTextField txtRegisterEmail;
    public JFXPasswordField txtRegisterPassword;
    public JFXTextField txtLoginEmail;
    public JFXPasswordField txtLoginPassword;
    public JFXButton btnLogin;
    public StackPane stackPaneLogin;
    public JFXComboBox<Locale> comboBoxLang;

    private InstitutionService institutionService;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        institutionService = new InstitutionService();

        comboBoxLang.setItems(FXCollections.observableArrayList(LocaleHelper.getImplementedLocales()).sorted());
        comboBoxLang.setConverter(LocaleHelper.getLocaleStringConverter());

        // Fallback language (English in this case) has no Display Name by default
        LocaleHelper.updateDefaultLang();
        comboBoxLang.setValue(Locale.getDefault());
    }

    @FXML
    protected void efetuateInstitutionLogin() {
        InstitutionAuth institutionAuth = new InstitutionAuth();
        boolean validLogin = institutionAuth.login(txtLoginEmail.getText(), txtLoginPassword.getText());

        if(!validLogin)
            showToastMessage(strings.getString("error.login"));
        else
            openHomeView();
    }

    @FXML
    protected void efetuateInstitutionRegister() {
        Institution newInstitution = new Institution(txtRegisterName.getText(), txtRegisterEmail.getText(),
                txtRegisterPassword.getText());

        String warning = InstitutionAuth.validatePasswordAndReturnMessage(
                txtRegisterPassword.getText(), txtRegisterPassword.getText());
        warning = txtRegisterName.getText().length() < 3 ? strings.getString("error.threeCharsName") : warning;

        if(warning == null){
            boolean validRegister = institutionService.insertInstitution(newInstitution);
            showToastMessage(validRegister ?
                    strings.getString("successRegister") : strings.getString("error.register"));
        } else
            showToastMessage(warning);
    }

    private void openHomeView(){
        Main.mainStage.close();
        Main.mainStage = new Stage();
        maximizeView();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/home/home.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            Scene scene = new Scene(root);

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
        String heading = strings.getString("resetPassword");
        String body = strings.getString("forgotPasswordDialogBody");
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

    @FXML
    private void updateLanguage() throws IOException {
        Locale.setDefault(comboBoxLang.getValue());
        strings = ResourceBundle.getBundle("bundles.lang", new UTF8Control());
        Parent root = FXMLLoader.load(getClass().getResource("/views/initial.fxml"), strings);
        Main.mainStage.getScene().setRoot(root);
    }
}
