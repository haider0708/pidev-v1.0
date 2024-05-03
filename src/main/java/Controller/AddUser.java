package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.Patient;
import Service.Service;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


public class AddUser implements Initializable {

    @FXML
    private TextField addressField;

    @FXML
    private TextField ageField;

    @FXML
    private Text age_numberERROR;

    @FXML
    private Text emailERROR;

    @FXML
    private TextField emailField;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField lastnameField;

    @FXML
    private Text nameERROR;

    @FXML
    private TextField numberField;

    @FXML
    private Text passwordERROR;

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<?> roleChoiceBox;

    @FXML
    private Text roleERROR;

    @FXML
    private ChoiceBox<?> sexeChoiceBox;

    @FXML
    private Text sexeERROR;
    @FXML
    private Pane pnlOrders;

    boolean ageValid = true;
    boolean numberValid = true;
    int age = 0;

    boolean isValid = true;

    @FXML
    void chooseFileButtonClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(((Stage) imageField.getScene().getWindow()));

        if (selectedFile != null) {
            imageField.setText(selectedFile.getAbsolutePath());
        }

    }

    @FXML
    void cancel(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View.fxml"));
            Pane addUserView = loader.load();
            pnlOrders.getChildren().setAll(addUserView);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Service Service;
    String query = null;
    private boolean update;
    int PatientId;

    public AddUser() {
        this.Service = new Service();
    }


    @FXML
    void save(MouseEvent event) {
        String email = emailField.getText();
        String role = (String) roleChoiceBox.getValue();
        String password = passwordField.getText();
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String sexe = (String) sexeChoiceBox.getValue();
        String ageText = ageField.getText();
        String number = numberField.getText();
        String img_path = imageField.getText();
        String address = addressField.getText();

        if (validateFields(email, role, password, firstname, lastname, sexe, ageText, number)) {
            try {
                int age = Integer.parseInt(ageText);
                String hashedPassword = hashPassword(password);
                String imagePath = copyAndRenameImage(img_path);

                if (update) {
                    updatePatient(email, role, hashedPassword, firstname, lastname, sexe, ageText, number, imagePath, address);
                } else {
                    addPatient(email, role, hashedPassword, firstname, lastname, sexe, ageText, number, imagePath, address);
                }

                redirectToView();
            } catch (NumberFormatException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePatient(String email, String role, String password, String firstname, String lastname, String sexe, String ageText, String number, String img_path, String address) {
        try {
            int age = Integer.parseInt(ageText);
            Patient updatedPatient = new Patient(PatientId, email, new String[]{role}, password, firstname, lastname, sexe, age, number, img_path, address, false, null);
            Service.update(updatedPatient);
            System.out.println("Patient updated successfully!");
        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    void setUpdate(boolean b) {
        this.update = b;

    }
    void setTextField(int id, String email,String password, String firstname, String lastname,
                      int age, String number, String img_path, String address) {
        PatientId = id;
        addressField.setText(address);
        ageField.setText(String.valueOf(age));
        emailField.setText(email);
        firstnameField.setText(firstname);
        imageField.setText(img_path);
        lastnameField.setText(lastname);
        numberField.setText(number);
        passwordField.setText(password);

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
    private String generateUniqueFileName(String fileName) {
        // Get file extension
        String extension = "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            extension = fileName.substring(dotIndex);
        }

        // Generate unique file name using UUID
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        return uniqueFileName;
    }

    private String copyAndRenameImage(String imgPath) throws IOException {
        File selectedFile = new File(imgPath);
        Path sourcePath = selectedFile.toPath();
        String fileName = generateUniqueFileName(selectedFile.getName());
        Path targetPath = new File("src/main/java/Images" + fileName).toPath();
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toAbsolutePath().toString();
    }

    private String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }

    private void redirectToView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View.fxml"));
            Pane addUserView = loader.load();
            pnlOrders.getChildren().setAll(addUserView);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validateFields(String email, String role, String password, String firstname, String lastname, String sexe, String ageText, String number) {


        if (email.isEmpty())  {
            emailERROR.setText("Please enter an email address.");
            isValid = false;
        } else if (!isValidEmail(email)) {
            emailERROR.setText("Please enter a valid email address.");
            isValid = false;
        } else {
            try {
                if (Service.emailExists(email)) {
                    emailERROR.setText("This email exist.");
                    isValid = false;
                } else {
                    emailERROR.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                isValid = false;
            }
        }


        if (role == null) {
            roleERROR.setText("Please select a role.");
            isValid = false;
        } else {
            roleERROR.setText("");
        }

        if (password.isEmpty()) {
            passwordERROR.setText("Please enter a password.");
            isValid = false;
        } else if (!validatePassword(password)) {
            passwordERROR.setText("Password must be at least 8 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one symbol.");
            isValid = false;
        } else {
            passwordERROR.setText("");
        }

        if (!firstname.isEmpty()) {
            if (!validateName(firstname)) {
                nameERROR.setText("First name must be at least 4 characters long and contain only alphabetic characters.");
                isValid = false;
            } else {
                nameERROR.setText("");
            }
        }

        if (!lastname.isEmpty()) {
            if (!validateName(lastname)) {
                if (!firstname.isEmpty() && !nameERROR.getText().isEmpty()) {
                    nameERROR.setText(nameERROR.getText() + "\nLast name must be at least 4 characters long and contain only alphabetic characters.");
                } else {
                    nameERROR.setText("Last name must be at least 4 characters long and contain only alphabetic characters.");
                }
                isValid = false;
            } else {
                if (!firstname.isEmpty() && !nameERROR.getText().isEmpty()) {
                    nameERROR.setText(nameERROR.getText() + "\n");
                } else {
                    nameERROR.setText("");
                }
            }
        }

        if (firstname.isEmpty() && lastname.isEmpty()) {
            nameERROR.setText("please provide your first and last names");
        }



        if (sexe == null) {
            sexeERROR.setText("Please select a gender.");
            isValid = false;
        } else {
            sexeERROR.setText("");
        }
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            ageValid = false;
        }

        if (age <= 0 && number.isEmpty()) {
            age_numberERROR.setText("Please enter your age and phone number.");
            isValid = false;
        } else {
            if (age <= 0) {
                age_numberERROR.setText("Please enter a valid age.");
                ageValid = false;
                isValid = false;
            } else {
                if (age < 10 || age > 99) {
                    age_numberERROR.setText("Age must be between 10 and 99.");
                    ageValid = false;
                    isValid = false;
                } else {
                    age_numberERROR.setText("");
                }
            }

            if (number.isEmpty()) {
                if (age_numberERROR.getText().isEmpty()) {
                    age_numberERROR.setText("Please enter a phone number.");
                } else {
                    age_numberERROR.setText(age_numberERROR.getText() + " Please enter a phone number.");
                }
                numberValid = false;
                isValid = false;
            } else {
                if (!number.matches("\\d{8}")) {
                    age_numberERROR.setText("");
                    if (age_numberERROR.getText().isEmpty()) {
                        age_numberERROR.setText("Phone number must be exactly 8 digits.");
                    } else {
                        age_numberERROR.setText(age_numberERROR.getText() + " Phone number must be exactly 8 digits.");
                    }
                    numberValid = false;
                    isValid = false;
                } else {
                    age_numberERROR.setText("");
                }
            }
        }

        if (ageValid && numberValid) {
            age_numberERROR.setText("");
        }




        return isValid;


    }
}
