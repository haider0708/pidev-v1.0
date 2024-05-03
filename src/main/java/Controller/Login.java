package Controller;

import Model.Patient;
import Service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import org.mindrot.jbcrypt.BCrypt;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class Login implements Initializable {

    @FXML
    private Hyperlink registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Service service;

    @FXML
    private Canvas captchaCanvas;

    @FXML
    private TextField captchaField;

    private int captchaNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.service = new Service();
        generateCaptcha();
    }



    @FXML
    protected void handleLoginButtonAction() {
        String email = usernameField.getText();
        String password = passwordField.getText();
        String captcha = captchaField.getText();

        if (!String.valueOf(captchaNumber).equals(captcha)) {
            System.out.println("Invalid CAPTCHA. Please try again");
            generateCaptcha();
            return;
        }

        try {
            Patient loggedInUser = service.Log(email, password);

            if (loggedInUser != null) {
                System.out.println("Login successful!");
                SessionManager.startSession(loggedInUser);
                System.out.println("Logged in user: " + loggedInUser.getEmail());

                FXMLLoader loader = null;
                if (loggedInUser.hasRole("User")) {
                    loader = new FXMLLoader(getClass().getResource("/Front.fxml"));
                } else if (loggedInUser.hasRole("Admin")) {
                    loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                }

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



    @FXML
    void Register() {
        try {
            // Get the current stage
            Stage currentStage = (Stage) registerButton.getScene().getWindow();

            // Close the current stage
            currentStage.close();

            // Load the FXML file for the new stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage registerStage = new Stage();
            registerStage.setTitle("Register");

            // Set the scene for the new stage
            Scene scene = new Scene(root);
            registerStage.setScene(scene);

            // Add animation (fade-in effect)
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();

            // Show the new stage
            registerStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }

    private void generateCaptcha() {
        GraphicsContext gc = captchaCanvas.getGraphicsContext2D();

        // Clear the canvas
        gc.clearRect(0, 0, captchaCanvas.getWidth(), captchaCanvas.getHeight());

        // Generate a random number
        Random random = new Random();
        captchaNumber = random.nextInt(9999 - 1000 + 1) + 1000; // generates a random number between 1000 and 9999

        // Draw the number on the canvas
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(48));
        gc.fillText(String.valueOf(captchaNumber), captchaCanvas.getWidth() / 2.0, captchaCanvas.getHeight() / 2.0);
    }


}
