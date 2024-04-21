package Controller;

import Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Login {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Service service;

    public Login() {
        this.service = new Service();
    }

    @FXML
    protected void handleLoginButtonAction() {
        String email = usernameField.getText();
        String password = passwordField.getText();
        try {
            boolean loggedIn = service.login(email, password);

            if (loggedIn) {
                System.out.println("Login successful!");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                Parent homeRoot = loader.load();
                Scene homeScene = new Scene(homeRoot);


                Stage stage = (Stage) usernameField.getScene().getWindow();


                stage.setScene(homeScene);
                stage.show();
            } else {
                System.out.println("Invalid email or password. Please try again");
            }
        } catch (SQLException | IOException e) {
            System.err.println("Error during login: " + e.getMessage());
        }
    }

}
