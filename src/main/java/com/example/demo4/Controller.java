package com.example.demo4;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller implements Initializable {

    @FXML
    private Label NAME;

    @FXML
    private ImageView IMAGE;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*

        if (currentPatient != null) {
            NAME.setText(currentPatient.getFirstname() + " " + currentPatient.getLastname());
            String imgPath = currentPatient.getImg_path();

            Image image = new Image(imgPath);

            IMAGE.setImage(image);
        }
*/
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnOrders) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo4/ajouterlivreur.fxml"));
                Pane addUserView = loader.load();
                pnlOrders.getChildren().setAll(addUserView);
                pnlOrders.toFront();
            } catch (IOException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    void LOGOUT(MouseEvent event) {
        /*try {
            SessionManager.endSession();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            Scene loginScene = new Scene(root);

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.setScene(loginScene);

            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}

