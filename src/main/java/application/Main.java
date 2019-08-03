package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.MongoConnection;

public class Main extends Application {
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/initial.fxml"));
        Scene scene = new Scene(root);
        mainStage = stage;

        stage.setTitle("Gereco");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        MongoConnection mongoConnection = new MongoConnection();
        mongoConnection.createConnection();

        launch(args);
    }
}
