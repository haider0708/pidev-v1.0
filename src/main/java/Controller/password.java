package Controller;

import Model.Patient;
import Service.Service;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class password implements Initializable {
    @FXML
    private TextField confirmpass;

    @FXML
    private TextField newpass;

    @FXML
    private TextField oldpass;

    @FXML
    private Text passError;

    @FXML
    void cancel(MouseEvent event) {

    }

    @FXML
    void save(MouseEvent event) throws SQLException {
        Patient currentPatient = SessionManager.getCurrentSession(); // replace with your method to get the current user

        if (currentPatient != null) {
            String oldPassword = oldpass.getText();
            String newPassword = newpass.getText();
            String confirmPassword = confirmpass.getText();

            // Check if the old password is correct
            if (BCrypt.checkpw(oldPassword, currentPatient.getPassword())) {
                // Check if the new password and confirm password match
                if (newPassword.equals(confirmPassword)) {
                    // Hash the new password
                    String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                    // Update the patient's password
                    currentPatient.setPassword(hashedPassword);
                    Service service = new Service();
                    service.update(currentPatient);
                } else {
                    passError.setText("New password and confirm password do not match.");
                }
            } else {
                passError.setText("Old password is incorrect.");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Patient currentPatient = SessionManager.getCurrentSession();
    }
}
