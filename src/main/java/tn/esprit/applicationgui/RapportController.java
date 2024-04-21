package tn.esprit.applicationgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RapportController implements Initializable {
    Connection con = null;
    PreparedStatement st1 = null;
    ResultSet rs1 = null;

    @FXML
    private Button btnDelete2;

    @FXML
    private Button btnSave2;

    @FXML
    private Button btnUpdate2;

    @FXML
    private TextField tNote;

    @FXML
    private ChoiceBox<String> tType1;

    @FXML
    private ChoiceBox<Integer> tRdvId;

    @FXML
    private TextField tType2;

    @FXML
    private TableColumn<Rapport, Integer> colID1;

    @FXML
    private TableColumn<Rapport, String> colNote;

    @FXML
    private TableColumn<Rapport, String> colType;

    @FXML
    private TableColumn<Rapport, Integer> colRdvId;

    @FXML
    private TableView<Rapport> table2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DBConnection.getCon();
        showRapports();

        // Charger les identifiants des rendez-vous depuis la base de données et les affecter au ChoiceBox
        loadRdvIdsFromDatabase();

        // Ajouter les choix au ChoiceBox tType1
        tType1.setItems(FXCollections.observableArrayList(
                "resultat_analyses",
                "consultation_psychologique",
                "diabetique"
        ));

        // Ajouter un listener au ChoiceBox tType1 pour détecter les changements de sélection
        tType1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Mettre à jour le TextField tType2 avec le type sélectionné dans tType1
            tType2.setText(newValue);
        });
    }

    private void loadRdvIdsFromDatabase() {
        ObservableList<Integer> rdvIdList = FXCollections.observableArrayList();
        String query = "SELECT ID FROM rendezvous";
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int rdvId = rs.getInt("ID");
                rdvIdList.add(rdvId);
            }
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de la récupération des identifiants de rendez-vous.", e);
        }
        tRdvId.setItems(rdvIdList);
    }

    @FXML
    public void CreateRapport(ActionEvent actionEvent) {
        String note = tNote.getText().trim();
        String type = tType1.getValue(); // Récupérer la valeur sélectionnée dans tType1

        // Validation de la saisie
        if (!validateInput(note, type)) {
            return;
        }

        // Insérer dans la base de données
        String insert = "INSERT INTO rapport(Note, Type, rdv_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement st = con.prepareStatement(insert);
            st.setString(1, note);
            st.setString(2, type);
            st.setInt(3, tRdvId.getValue()); // Récupérer la valeur sélectionnée dans tRdvId
            st.executeUpdate();
            clear();
            showSuccessAlert("Rapport enregistré", "Le rapport a été enregistré avec succès.");
            // Actualiser l'affichage après l'ajout
            showRapports();
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de l'insertion du rapport.", e);
        }
    }

    // Méthode pour valider la saisie
    private boolean validateInput(String note, String type) {
        if (!note.matches("[a-zA-Z ]+")) {
            showAlert("Format de note invalide", "La note ne doit contenir que des caractères alphabétiques.");
            return false;
        }
        if (!isValidType(type)) {
            showAlert("Type invalide", "Le type doit être l'un des choix proposés.");
            return false;
        }
        return true;
    }

    // Méthode pour valider le type
    private boolean isValidType(String type) {
        return type.equals("resultat_analyses") ||
                type.equals("consultation_psychologique") ||
                type.equals("diabetique");
    }

    void showRapports() {
        ObservableList<Rapport> rapportList = FXCollections.observableArrayList();
        String query = "SELECT rapport.*, rendezvous.ID AS rdvId FROM rapport " +
                "JOIN rendezvous ON rapport.rdv_id = rendezvous.ID";
        try {
            PreparedStatement st = con.prepareStatement(query);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String note = rs.getString("Note");
                String type = rs.getString("Type");
                int rdvId = rs.getInt("rdvId");
                // Création d'un nouvel objet Rapport et ajout à la liste observable
                Rapport rapport = new Rapport(id, note, type, rdvId);
                rapportList.add(rapport);
            }
        } catch (SQLException e) {
            handleException("Erreur SQL", "Une erreur s'est produite lors de la récupération des rapports.", e);
        }
        // Configuration de la table pour afficher les rapports
        colID1.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRdvId.setCellValueFactory(new PropertyValueFactory<>("rdvId")); // Assurez-vous d'avoir la colonne dans la TableView
        // Affectation de la liste observable à la table
        table2.setItems(rapportList);
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

    void handleException(String title, String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message + "\n" + e.getMessage());
        alert.showAndWait();
    }

    void clear() {
        tNote.setText(null);
        tType1.setValue(null);
        tType2.setText(null);
        tRdvId.setValue(null);
    }

    public void UpdateRapport(ActionEvent actionEvent) {
        Rapport selectedRapport = table2.getSelectionModel().getSelectedItem();
        if (selectedRapport != null) {
            // Remplir les champs avec les valeurs du rapport sélectionné
            tNote.setText(selectedRapport.getNote());
            tType1.setValue(selectedRapport.getType());
            tRdvId.setValue(selectedRapport.getRdvId());

            btnUpdate2.setDisable(false); // Activer le bouton de mise à jour

            showSuccessAlert("Rapport sélectionné", "Vous pouvez maintenant modifier le rapport sélectionné.");
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un rapport à mettre à jour.");
        }
    }


    public void DeleteRapport(ActionEvent actionEvent) {
        Rapport selectedRapport = table2.getSelectionModel().getSelectedItem();
        if (selectedRapport != null) {
            // Supprimer le rapport de la base de données
            String deleteQuery = "DELETE FROM rapport WHERE ID=?";
            try {
                PreparedStatement st = con.prepareStatement(deleteQuery);
                st.setInt(1, selectedRapport.getId());
                st.executeUpdate();
                clear();
                showSuccessAlert("Rapport supprimé", "Le rapport a été supprimé avec succès.");
                // Actualiser l'affichage après la suppression
                showRapports();
            } catch (SQLException e) {
                handleException("Erreur SQL", "Une erreur s'est produite lors de la suppression du rapport.", e);
            }
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un rapport à supprimer.");
        }
    }
}
