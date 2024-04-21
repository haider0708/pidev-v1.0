package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/file.fxml")); // Make sure this is the correct path to your initial FXML
        Parent root = loader.load();
        Scene scene = new Scene(root, 800, 600); // Adjust size as needed
        stage.setTitle("Event Management System"); // You can set a relevant title
        stage.setScene(scene);
        stage.show();
    }
}
