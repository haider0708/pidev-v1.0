package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class FxMain extends Application{
    public  static void main(String[] args) {
    launch();
}

    @Override
    public void start(Stage stage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPharmacieFXML.fxml"));
        Parent parent = loader.load();
        FXMLLoader ordonnanceLoader = new FXMLLoader(getClass().getResource("/OrdonnanceFXML.fxml"));
        Parent ordonnanceParent = ordonnanceLoader.load();
        Scene scene = new Scene(parent);
        Scene ordonnanceScene = new Scene(ordonnanceParent);

        stage.setTitle("Ajouter une pharmacie ");
        stage.setScene(scene);
        Stage ordonnanceStage = new Stage();
        ordonnanceStage.setTitle("Ordonnance");
        ordonnanceStage.setScene(ordonnanceScene);
        stage.show();
        ordonnanceStage.show();

    }
}
