package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxMain extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduitFXML.fxml"));
        Parent parent = loader.load();
        FXMLLoader Categorieloader = new FXMLLoader(getClass().getResource("/CategorieFXML.fxml"));
        Parent Categorieparent = Categorieloader.load();

        Scene scene = new Scene(parent);
        Scene Categoriescene = new Scene(Categorieparent);

        stage.setTitle("Ajouter un produit");
        stage.setScene(scene);

        Stage Categoriestage = new Stage();

        Categoriestage.setScene(Categoriescene);

        stage.show();
        Categoriestage.show();
    }
}
