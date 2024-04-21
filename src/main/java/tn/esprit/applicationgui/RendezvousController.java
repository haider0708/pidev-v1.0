package tn.esprit.applicationgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class RendezvousController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tLieu;

    @FXML
    private TableColumn<Rendezvous, Date> colDate;

    @FXML
    private TableColumn<Rendezvous, String> colDescription;

    @FXML
    private TableColumn<Rendezvous, Integer> colId;

    @FXML
    private TableColumn<Rendezvous, String> colLieu;

    @FXML
    private TableView<Rendezvous> table;
int id =0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showRendezvous();
    }

    public ObservableList<Rendezvous> getRendezvous() {
        ObservableList<Rendezvous> rendezvousList = FXCollections.observableArrayList();
        String query = "SELECT * FROM rendezvous";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Rendezvous rendezvous = new Rendezvous();
                rendezvous.setId(rs.getInt("id"));
                rendezvous.setLieu(rs.getString("Lieu"));
                rendezvous.setDescription(rs.getString("description"));
                rendezvous.setDate(rs.getDate("date"));
                rendezvousList.add(rendezvous);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rendezvousList;
    }

    public void showRendezvous() {
        ObservableList<Rendezvous> list = getRendezvous();
        table.setItems(list);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    @FXML
    private TextField tdate;

    @FXML
    private TextField tdescription;

    @FXML
    void clearField(ActionEvent event) {
        clear();
    }

    @FXML
    void createRendezvous(ActionEvent event) {
        String lieu = tLieu.getText().trim();
        String description = tdescription.getText().trim();
        String dateStr = tdate.getText().trim();

        // Vérifier si le lieu, la description et la date sont non vides
        if (lieu.isEmpty() || description.isEmpty() || dateStr.isEmpty()) {
            showAlert("Champ(s) vide(s)", "Veuillez remplir tous les champs.");
            return;
        }

        // Vérifier si le lieu contient uniquement des caractères alphabétiques
        if (!lieu.matches("[a-zA-Z]+")) {
            showAlert("Lieu invalide", "Le lieu ne peut contenir que des lettres.");
            return;
        }

        // Vérifier si la description contient uniquement des caractères alphabétiques
        if (!description.matches("[a-zA-Z]+")) {
            showAlert("Description invalide", "La description ne peut contenir que des lettres.");
            return;
        }

        // Vérifier si la date est une chaîne de caractères non vide et au format valide
        if (dateStr.isEmpty()) {
            showAlert("Date invalide", "Veuillez saisir une date valide.");
            return;
        }

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            showAlert("Date invalide", "Veuillez saisir une date au format YYYY-MM-DD.");
            return;
        }

        // Vérifier si la date est dans le futur ou égale à aujourd'hui
        LocalDate today = LocalDate.now();
        if (localDate.isBefore(today)) {
            showAlert("Date invalide", "Veuillez saisir une date future ou égale à aujourd'hui.");
            return;
        }

        // Si toutes les vérifications passent, insérer dans la base de données
        String insert = "insert into rendezvous(Lieu,description,date) values(?,?,?)";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(insert);
            st.setString(1, lieu);
            st.setString(2, description);
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
            st.setDate(3, sqlDate);
            st.executeUpdate();
            clear();
            showRendezvous();
            showSuccessAlert("Rendez-vous enregistré", "Le rendez-vous a été enregistré avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void getData(MouseEvent event) {
     Rendezvous rendezvous = table.getSelectionModel().getSelectedItem();
     id = rendezvous.getId();
     tLieu.setText(rendezvous.getLieu());
     tdescription.setText(rendezvous.getDescription());
     tdate.setText(rendezvous.getDate().toString());
     btnSave.setDisable(true);
    }
void clear(){
        tLieu.setText(null);
        tdescription.setText(null);
        tdate.setText(null);
        btnSave.setDisable(false);

}
    @FXML
    void deleteRendezvous(ActionEvent event) {
        String delete = "delete from rendezvous where id = ?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(delete);
            st.setInt(1,id);
            st.executeUpdate();
            showRendezvous();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void updateRendezvous(ActionEvent event) {
        String update = "update rendezvous set Lieu = ?, description = ?, date = ? where id =?";
        con = DBConnection.getCon();
        try {
            st = con.prepareStatement(update);
            st.setString(1,tLieu.getText());
            st.setString(2,tdescription.getText());
            LocalDate localDate = LocalDate.parse(tdate.getText());
            java.sql.Date date = java.sql.Date.valueOf(localDate);
            st.setDate(3, date);
            st.setInt(4,id );
            st.executeUpdate();
            showRendezvous();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
