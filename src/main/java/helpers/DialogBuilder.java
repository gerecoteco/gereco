package helpers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogBuilder {
    private String headingText;
    private String bodyText;
    private StackPane dialogContainer;

    private JFXButton btnConfirm;

    public DialogBuilder(String headingText, String bodyText, StackPane dialogContainer) {
        this.headingText = headingText;
        this.bodyText = bodyText;
        this.dialogContainer = dialogContainer;
    }

    private String getHeadingText() {
        return headingText;
    }
    private String getBodyText() {
        return bodyText;
    }
    private StackPane getDialogContainer() {
        return dialogContainer;
    }

    public JFXDialog createDialogAndReturn(){
        JFXDialogLayout content = new JFXDialogLayout();
        btnConfirm = new JFXButton("Confirmar");
        JFXButton btnCancel = new JFXButton("Cancelar");
        HBox hbox = new HBox(5);
        JFXDialog dialog;

        content.setHeading(new Label(getHeadingText()));
        content.setBody(new Text(getBodyText()));

        hbox.getChildren().add(btnCancel);
        hbox.getChildren().add(btnConfirm);
        content.setActions(hbox);

        dialog = new JFXDialog(getDialogContainer(), content, JFXDialog.DialogTransition.CENTER);
        btnCancel.setOnAction(action -> dialog.close());

        return dialog;
    }

    public void setConfirmAction(EventHandler confirmEventHandler){
        btnConfirm.setOnAction(confirmEventHandler);
    }
}
