package Controller;

import Model.Patient;
import Service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class Register {

    @FXML
    private Text AddressERR;

    @FXML
    private Text EmailERR;

    @FXML
    private Text FirstnameERR;

    @FXML
    private Text NumberERR;

    @FXML
    private Text PasswordERR;

    @FXML
    private Text ageERR;

    @FXML
    private Text lastnameERR;

    @FXML
    private Text sexeERR;

    @FXML
    private TextField addressField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField numberField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<?> sexeChoiceBox;

    boolean ageValid = true;
    boolean numberValid = true;
    int age = 0;
    boolean isValid = true;
    private Service Service;

    public Register() {
        this.Service = new Service();
    }


    @FXML
    void Register(MouseEvent event) {
        String email = emailField.getText();
        String role ="USER";
        String password = passwordField.getText();
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String sexe = (String) sexeChoiceBox.getValue();
        String ageText = ageField.getText();
        String number = numberField.getText();
        String img_path = imageField.getText();
        String address = addressField.getText();
        if (validateFields(email,password,firstname,lastname,sexe,ageText,number,address)) {
                addPatient(email, role , password , firstname , lastname , sexe , ageText , number , img_path , address);
            }
        else {
            System.out.println("Patient not added");
        }
        }


    private void addPatient(String email, String role, String password, String firstname, String lastname, String sexe, String ageText, String number, String img_path, String address) {
        try {
            int age = Integer.parseInt(ageText);
            Patient newPatient = new Patient(0, email, new String[]{role}, password, firstname, lastname, sexe, age, number, img_path, address, false, null);
            Service.ajouter(newPatient);
            System.out.println("Patient added successfully!");
        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
        }
    }

    private boolean validateFields(String email,String password, String firstname, String lastname, String sexe, String ageText, String number , String address ) {


        if (email.isEmpty())  {
            EmailERR.setText("Please enter an email address.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            EmailERR.setText("Please enter a valid email address.");
            isValid = false;
        } else {
            try {
                if (Service.emailExists(email)) {
                    EmailERR.setText("This email exist.");
                    isValid = false;
                } else {
                    EmailERR.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                isValid = false;
            }
        }
        if (password.isEmpty()) {
            PasswordERR.setText("Please enter a password.");
            isValid = false;
        } else if (!validatePassword(password)) {
            PasswordERR.setText("Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one symbol.");
            isValid = false;
        } else {
            PasswordERR.setText("");
        }

        if (!firstname.isEmpty()) {
            if (!validateName(firstname)) {
                FirstnameERR.setText("First name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                FirstnameERR.setText("");
            }
        } else {
            FirstnameERR.setText("Please provide your first name.");
            isValid = false;
        }

        if (!lastname.isEmpty()) {
            if (!validateName(lastname)) {
                lastnameERR.setText("Last name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                lastnameERR.setText("");
            }
        } else {
            lastnameERR.setText("Please provide your Last name.");
            isValid = false;
        }

        if (sexe == null) {
            sexeERR.setText("Please select a gender.");
            isValid = false;
        } else {
            sexeERR.setText("");
        }

        if (!ageText.isEmpty()) {
            try {
                int age = Integer.parseInt(ageText);
                if (age < 10 || age > 99) { // Ensure age is between 10 and 99
                    ageERR.setText("Age must be exactly two digits.");
                    isValid = false;
                } else {
                    ageERR.setText("");
                }
            } catch (NumberFormatException e) {
                ageERR.setText("Please enter a valid age.");
                isValid = false;
            }
        } else {
            ageERR.setText("Please enter your age.");
            isValid = false;
        }

        if (!number.isEmpty()) {
            if (!number.matches("\\d{8}")) { // Check if number contains exactly 8 digits
                NumberERR.setText("Phone number must be exactly 8 digits.");
                isValid = false;
            } else {
                NumberERR.setText("");
            }
        } else {
            NumberERR.setText("Please enter a phone number.");
            isValid = false;
        }

        if (address.isEmpty()) {
            AddressERR.setText("Please enter an address.");
            isValid = false;
        } else {
            AddressERR.setText("");
        }
        return isValid;


    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                hasLower = true;
            } else if (Character.isUpperCase(ch)) {
                hasUpper = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSymbol(ch)) {
                hasSymbol = true;
            }
        }

        // Check if all required character types are present
        return hasLower && hasUpper && hasDigit && hasSymbol;
    }

    private boolean isSymbol(char ch) {
        String symbols = "!@#$%^&*()-_+=[]{}|;:,.<>?";
        return symbols.indexOf(ch) != -1;
    }

    private boolean validateName(String name) {
        if (name.isEmpty()) {
            return false;
        }
        return name.length() >= 4 && name.matches("[a-zA-Z]+");
    }

}
