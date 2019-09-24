package controllers;

import application.Session;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import services.InstitutionService;

import static controllers.EventItemController.deleteEvent;
import static helpers.InstitutionAuth.encryptPassword;

public class PasswordValidationController {
    public JFXPasswordField txtPassword;
    public JFXButton btnConfirm;

    static boolean deleteInstitution;

    @FXML
    public void initialize(){
        btnConfirm.setOnAction(action -> {
            String password = encryptPassword(txtPassword.getText());
            if(password.equals(Session.getInstance().getInstitution().getPassword())){
                closeStage();
                if(deleteInstitution) {
                    new InstitutionService().deleteInstitution();
                    HomeController.returnToInitialView();
                } else deleteEvent();
            } else HomeController.showToastMessage("Senha incorreta!");
        });
    }

    private void closeStage(){
        ((Stage) btnConfirm.getScene().getWindow()).close();
    }
}
