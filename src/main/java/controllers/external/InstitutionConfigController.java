package controllers.external;

import application.Session;
import com.jfoenix.controls.*;
import controllers.home.HomeController;
import helpers.DialogBuilder;
import helpers.UTF8Control;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Institution;
import services.InstitutionService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controllers.external.PasswordValidationController.deleteInstitution;
import static helpers.InstitutionAuth.encryptPassword;
import static helpers.InstitutionAuth.validatePasswordAndReturnMessage;

public class InstitutionConfigController implements Initializable {
    public Label lblInstitutionName;
    public StackPane stackPaneMain;
    public JFXTextField txtInstitutionName;
    public JFXPasswordField txtNewPassword;
    public JFXPasswordField txtConfirmPassword;
    public JFXPasswordField txtActualPassword;

    private Institution institutionLogged;
    private ResourceBundle strings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        strings = resources;
        institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionName.setText(institutionLogged.getName());
    }

    @FXML
    protected void updateName(){
        if(txtInstitutionName.getText().isEmpty())
            showToastMessage(strings.getString("error.emptyFields"));
        else if(txtInstitutionName.getText().length() < 3)
            showToastMessage(strings.getString("error.threeCharsName"));
        else
            loadUpdateNameDialog();
    }

    private void loadUpdateNameDialog(){
        String heading = strings.getString("updateNameDialog.heading");
        String body = strings.getString("updateNameDialog.body") +
                "\n(" + institutionLogged.getName() + " -> " + txtInstitutionName.getText() + ")";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, stackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            updateInstitutionName();
            HomeController.loadInstitutionInfoOnLabel();
            HomeController.showToastMessage(strings.getString("successNameUpdate"));
            closeStage();
        });
        dialog.show();
    }

    @FXML
    protected void deleteInstitution(){
        String heading = strings.getString("deleteAccount");
        String body = strings.getString("deleteInstitutionDialog.body");
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, stackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            loadPasswordValidationView();
            dialog.close();
            closeStage();
        });
        dialog.show();
    }

    @FXML
    protected void updatePassword(){
        String actualPassword = encryptPassword(txtActualPassword.getText());
        String warning = validatePasswordAndReturnMessage(txtNewPassword.getText(), txtConfirmPassword.getText());

        if(!actualPassword.equals(institutionLogged.getPassword()))
            warning = "Senha atual incorreta!";

        if(warning == null) loadUpdatePasswordDialog();
        else showToastMessage(warning);
    }

    private void loadPasswordValidationView(){
        deleteInstitution = true;

        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/views/external-forms/password-validation.fxml"),
                    ResourceBundle.getBundle("bundles.lang", new UTF8Control()));
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUpdatePasswordDialog(){
        String heading = strings.getString("updatePasswordDialog.heading");
        String body = strings.getString("updatePasswordDialog.body");
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, stackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            updateInstitutionPassword();
            HomeController.showToastMessage(strings.getString("successPasswordUpdate"));
            closeStage();
        });
        dialog.show();
    }

    private void updateInstitutionName(){
        String newName = txtInstitutionName.getText();
        institutionLogged.setName(newName);
        new InstitutionService().updateInstitution(institutionLogged);
    }

    private void updateInstitutionPassword(){
        String newPassword = encryptPassword(txtNewPassword.getText());
        institutionLogged.setPassword(newPassword);
        new InstitutionService().updateInstitution(institutionLogged);
    }

    private void showToastMessage(String messsage) {
        JFXSnackbar snackbar = new JFXSnackbar(stackPaneMain);
        snackbar.getStylesheets().add(getClass().getResource("/css/snackbar.css").toString());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(messsage, "OK", action -> snackbar.close()),
                Duration.millis(3000), null));
    }

    private void closeStage(){
        Stage actualStage = (Stage) stackPaneMain.getScene().getWindow();
        actualStage.close();
    }
}
