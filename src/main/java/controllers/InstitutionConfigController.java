package controllers;

import application.Session;
import com.jfoenix.controls.*;
import helpers.DialogBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Institution;
import services.InstitutionService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static helpers.InstitutionAuth.encryptPassword;

public class InstitutionConfigController {
    public Label lblInstitutionName;
    public StackPane stackPaneMain;
    public JFXTextField txtInstitutionName;
    public JFXPasswordField txtNewPassword;
    public JFXPasswordField txtConfirmPassword;
    public JFXPasswordField txtActualPassword;

    private Institution institutionLogged;

    @FXML
    public void initialize() {
        institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionName.setText(institutionLogged.getName());
    }

    @FXML
    protected void updateName(){
        if(txtInstitutionName.getText().isEmpty())
            showToastMessage("Preencha o campo corretamente!");
        else if(txtInstitutionName.getText().length() < 3)
            showToastMessage("O nome deve conter pelo menos 3 caracteres!");
        else
            loadUpdateNameDialog();
    }

    private void loadUpdateNameDialog(){
        String heading = "Alterar nome";
        String body = "Tem certeza que deseja mudar o nome da instituição de \"" + institutionLogged.getName() +
                "\" para \"" + txtInstitutionName.getText() + "\"?";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, stackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            updateInstitutionName();
            HomeController.loadInstitutionInfoOnLabel();
            HomeController.showToastMessage("Nome alterado com sucesso!");
            closeStage();
        });
        dialog.show();
    }

    @FXML
    protected void updatePassword(){
        String actualPassword = encryptPassword(txtActualPassword.getText());

        if(!actualPassword.equals(institutionLogged.getPassword()))
            showToastMessage("Senha atual incorreta!");
        else if(txtNewPassword.getText().isEmpty())
            showToastMessage("Preencha o campo!");
        else if(txtNewPassword.getText().length() < 6)
            showToastMessage("A Senha deve conter ao menos 6 caracteres!");
        else if(!passwordContainsSpecialCharacter())
            showToastMessage("A Senha deve conter caracter especial!");
        else if(!txtNewPassword.getText().equals(txtConfirmPassword.getText()))
            showToastMessage("As senhas estão diferentes!");
        else
            loadUpdatePasswordDialog();
    }

    private boolean passwordContainsSpecialCharacter(){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");

        String newPassword = txtNewPassword.getText();
        Matcher matcher = pattern.matcher(newPassword);

        return !matcher.matches();
    }

    private void loadUpdatePasswordDialog(){
        String heading = "Alterar senha";
        String body = "Tem certeza que deseja alterar sua senha?";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, stackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        dialogBuilder.setConfirmAction(action -> {
            updateInstitutionPassword();
            HomeController.showToastMessage("Senha alterada com sucesso!");
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
