package tn.esprit.applicationgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RapportApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Charger l'interface de rendez-vous
        Parent root = FXMLLoader.load(getClass().getResource("Rendezvous.fxml"));
        // Charger l'interface du rapport
        Parent rapportRoot = FXMLLoader.load(getClass().getResource("Rapport.fxml"));

        // Créer une nouvelle scène contenant l'interface de rendez-vous
        Scene scene = new Scene(root);
        // Créer une nouvelle scène contenant l'interface du rapport
        Scene rapportScene = new Scene(rapportRoot);

        // Ajouter le style CSS si nécessaire
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        rapportScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Afficher la scène de rendez-vous au démarrage de l'application
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rendez-vous");
        primaryStage.show();

        // Créer une nouvelle fenêtre pour l'interface du rapport
        Stage rapportStage = new Stage();
        rapportStage.setScene(rapportScene);
        rapportStage.setTitle("Rapport");
        rapportStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
