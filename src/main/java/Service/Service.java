package Service;

import Model.Patient;
import Utils.DBConnection;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

import java.net.PasswordAuthentication;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class Service {

    private static Connection connection = null;

    public Service() {
        connection = DBConnection.getInstance().getCnx();
    }

    // Méthode pour ajouter un patient
    public static void ajouter(Patient patient) throws SQLException {
        String query = "INSERT INTO patient (email, roles, password, firstname, lastname, sexe, age, number, img_path, address, is_verified, reset_token) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, patient.getEmail());
            preparedStatement.setString(2, String.join(",", patient.getRoles()));
            preparedStatement.setString(3, patient.getPassword());
            preparedStatement.setString(4, patient.getFirstname());
            preparedStatement.setString(5, patient.getLastname());
            preparedStatement.setString(6, patient.getSexe());
            preparedStatement.setInt(7, patient.getAge());
            preparedStatement.setString(8, patient.getNumber());
            preparedStatement.setString(9, patient.getImg_path());
            preparedStatement.setString(10, patient.getAddress());
            preparedStatement.setBoolean(11, patient.getIs_verified());
            preparedStatement.setString(12, patient.getReset_token());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour mettre à jour les informations d'un patient
    public void update(Patient updatedPatient) throws SQLException {
        String query = "UPDATE patient SET email = ?, roles = ?, password = ?, firstname = ?, lastname = ?, sexe = ?, age = ?, number = ?, img_path = ?, address = ?, is_verified = ?, reset_token = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, updatedPatient.getEmail());
            preparedStatement.setString(2, String.join(",", updatedPatient.getRoles()));
            preparedStatement.setString(3, updatedPatient.getPassword());
            preparedStatement.setString(4, updatedPatient.getFirstname());
            preparedStatement.setString(5, updatedPatient.getLastname());
            preparedStatement.setString(6, updatedPatient.getSexe());
            preparedStatement.setInt(7, updatedPatient.getAge());
            preparedStatement.setString(8, updatedPatient.getNumber());
            preparedStatement.setString(9, updatedPatient.getImg_path());
            preparedStatement.setString(10, updatedPatient.getAddress());
            preparedStatement.setBoolean(11, updatedPatient.getIs_verified());
            preparedStatement.setString(12, updatedPatient.getReset_token());
            preparedStatement.setInt(13, updatedPatient.getId());
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un patient par son ID
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM patient WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer tous les patients
    public ArrayList<Patient> afficherAll() throws SQLException {
        ArrayList<Patient> Patients = new ArrayList<>();
        String query = "SELECT * FROM patient";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String[] roles = resultSet.getString("roles").split(",");
                Gson gson = new Gson();
                String rolesJson = gson.toJson(roles);
                Patient patient = new Patient(resultSet.getInt("id"),
                        resultSet.getString("email"),
                        rolesJson,
                        resultSet.getString("password"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("sexe"),
                        resultSet.getInt("age"),
                        resultSet.getString("number"),
                        resultSet.getString("img_path"),
                        resultSet.getString("address"),
                        resultSet.getBoolean("is_verified"),
                        resultSet.getString("reset_token"));

                Patients.add(patient);
            }
        }
        return Patients;
    }

    // Méthode pour vérifier les informations de connexion (email et mot de passe)
    public boolean login(String email, String password) throws SQLException {
        String query = "SELECT password FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    return BCrypt.checkpw(password, hashedPasswordFromDB);
                }
            }
        }
        return false;
    }


    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0; // If count is greater than 0, email exists in the database
                }
            }
        }
        return false;
    }

    public Patient Log(String email, String password) throws SQLException {
        String query = "SELECT * FROM patient WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password");
                    if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                        Patient patient = new Patient();
                        patient.setId(resultSet.getInt("id"));
                        patient.setEmail(resultSet.getString("email"));
                        patient.setRoles(resultSet.getString("role"));
                        patient.setPassword(resultSet.getString("password"));
                        patient.setFirstname(resultSet.getString("firstname"));
                        patient.setLastname(resultSet.getString("lastname"));
                        patient.setSexe(resultSet.getString("sexe"));
                        patient.setAge(resultSet.getInt("age"));
                        patient.setNumber(resultSet.getString("number"));
                        patient.setImg_path(resultSet.getString("img_path"));
                        patient.setAddress(resultSet.getString("address"));
                        patient.setIs_verified(resultSet.getBoolean("is_verified"));
                        patient.setResetToken(resultSet.getString("reset_token"));
                        return patient;
                    }
                }
            }
        }
        return null;
    }
}
