package controllers;

import application.Session;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import helpers.DialogBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.Institution;
import services.InstitutionService;

public class InstitutionConfigController {
    public Label lblInstitutionName;
    public StackPane stackPaneMain;
    public JFXTextField txtInstitutionName;

    private Institution institutionLogged;

    @FXML
    public void initialize() {
        institutionLogged = Session.getInstance().getInstitution();
        lblInstitutionName.setText(institutionLogged.getName());
    }

    @FXML
    protected void loadSaveNameDialog(){
        JFXButton btnConfirm = new JFXButton("Confirmar");
        String heading = "Salvar nome";
        String body = "Tem certeza que deseja mudar o nome da instituição de \"" + institutionLogged.getName() +
                "\" para \"" + txtInstitutionName.getText() + "\"?";
        DialogBuilder dialogBuilder = new DialogBuilder(heading, body, btnConfirm, stackPaneMain);

        JFXDialog dialog = dialogBuilder.createDialogAndReturn();
        btnConfirm.setOnAction(action -> {
            changeInstitutionName();
            HomeController.loadInstitutionInfoOnLabel();
            closeStage();
        });
        dialog.show();
    }

    private void closeStage(){
        Stage actualStage = (Stage) stackPaneMain.getScene().getWindow();
        actualStage.close();
    }

    private void changeInstitutionName(){
        InstitutionService institutionService = new InstitutionService();
        String newName = txtInstitutionName.getText();

        institutionLogged.setName(newName);
        institutionService.updateInstitution(institutionLogged);
    }
}
