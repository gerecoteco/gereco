package helpers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class DialogBuilder {
    private String headingText;
    private String bodyText;
    private JFXButton confirmButton;
    private StackPane dialogContainer;

    public DialogBuilder(String headingText, String bodyText, JFXButton confirmButton, StackPane dialogContainer) {
        this.headingText = headingText;
        this.bodyText = bodyText;
        this.confirmButton = confirmButton;
        this.dialogContainer = dialogContainer;
    }

    private String getHeadingText() {
        return headingText;
    }
    private String getBodyText() {
        return bodyText;
    }
    private JFXButton getConfirmButton() {
        return confirmButton;
    }
    private StackPane getDialogContainer() {
        return dialogContainer;
    }

    public JFXDialog createDialogAndReturn(){
        JFXDialogLayout content = new JFXDialogLayout();
        JFXButton btnCancel = new JFXButton("Cancelar");
        HBox hbox = new HBox(5);
        JFXDialog dialog;

        content.setHeading(new Label(getHeadingText()));
        content.setBody(new Text(getBodyText()));

        hbox.getChildren().add(btnCancel);
        hbox.getChildren().add(getConfirmButton());
        content.setActions(hbox);

        dialog = new JFXDialog(getDialogContainer(), content, JFXDialog.DialogTransition.CENTER);

        btnCancel.setOnAction(action -> dialog.close());

        return dialog;
    }
}
