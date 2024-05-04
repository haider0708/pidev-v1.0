package Controller;

import Model.Patient;
import Service.Service;
import Service.InfobipService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Random;


public class ForgetPassword implements Initializable {
    private Service Service;

    private InfobipService Sms;

    @FXML
    private Button Code;

    @FXML
    private TextField CodeField;

    @FXML
    private TextField ConfirmPassword;

    @FXML
    private TextField EmailField;

    @FXML
    private TextField NewPassword;

    @FXML
    private Button Search;

    @FXML
    private Button confirm;

    @FXML
    void code(ActionEvent event) {
        String enteredCode = CodeField.getText();
        if (enteredCode.equals(SessionManager.getCodeS())) {
            ConfirmPassword.setVisible(true);
            NewPassword.setVisible(true);
            confirm.setVisible(true);
        } else {
            System.out.println("Incorrect code. Please try again");
        }
    }


    @FXML
    void confirm(ActionEvent event) throws SQLException {
        String newPassword = NewPassword.getText();
        String confirmPassword = ConfirmPassword.getText();
        if (newPassword.equals(confirmPassword)) {
            String hashedPassword = hashPassword(newPassword);
            SessionManager.getCurrentSession().setPassword(hashedPassword);
            Service.update(SessionManager.getCurrentSession());
        } else {
            System.out.println("Passwords do not match. Please try again");
        }
    }


    @FXML
    void search(ActionEvent event) throws SQLException, IOException {
        String email = EmailField.getText();
        // Retrieve the user data using the email
        Patient patient = Service.getPatientByEmail(email);
        if (patient != null) {
            SessionManager.startSession(patient);
            Code.setVisible(true);
            CodeField.setVisible(true);
            String code = generateCode();
            SessionManager.saveCodeS(code);
            Sms.sendSMS("216"+patient.getNumber(), code);
        } else {
            System.out.println("No user found with this email");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.Service = new Service();
        this.Sms = new InfobipService();
        Code.setVisible(false);
        CodeField.setVisible(false);
        ConfirmPassword.setVisible(false);
        NewPassword.setVisible(false);
        confirm.setVisible(false);
    }



        public static String generateCode() {
            Random random = new Random();
            int code = random.nextInt(900000) + 100000; // This will generate a random 6-digit number
            return String.valueOf(code);
        }

    private String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }



}
