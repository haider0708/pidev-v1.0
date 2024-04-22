package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Ordonnance;
import models.Pharmacie;
import utils.DBConnection;

import java.sql.Date;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrdonnanceFXML implements Initializable {
    /* private Connection cnx;
     private PreparedStatement st;
     private ResultSet rs;*/
    Connection cnx = null;
    PreparedStatement st = null;
    ResultSet rs = null;


    @FXML
    private TableView<Ordonnance> t;

    @FXML
    private TableColumn<Ordonnance, Date> colDate;

    @FXML
    private TableColumn<Ordonnance, String> colDescription;

    @FXML
    private TableColumn<Ordonnance, String> colMedecin;

    @FXML
    private TableColumn<Ordonnance, String> colPatient;
    @FXML
    private TableColumn<Ordonnance, Integer> colId;

    @FXML
    private TableColumn<Ordonnance, String> colNompharmacie;

    @FXML
    private Button btnAjouter;

    @FXML
    private DatePicker tfdate;

    @FXML
    private TextField tfdescription;

    @FXML
    private TextField tfmedecin;

    @FXML
    private TextField tfpatient;
    @FXML
    private ComboBox<String> comboph;

    int id = 0;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        cnx = DBConnection.getInstance().getCnx();
try{
        String sql = "SELECT DISTINCT nom FROM pharmacie";
        PreparedStatement statement = cnx.prepareStatement(sql);

    ResultSet rs = statement.executeQuery();
        List<String> pharmacies = new ArrayList<>();
        while (rs.next()) {
            pharmacies.add(rs.getString("nom"));
        }
        ObservableList<String> options = FXCollections.observableArrayList(pharmacies);
        comboph.setItems(options);
    } catch(
    SQLException e)

    {
        e.printStackTrace();  // Gérer l'exception selon vos besoins
    }

    showOrdonnance();
    t.getSelectionModel().

    selectedItemProperty().

    addListener((obs, oldSelection, newSelection) ->

    {
        if (newSelection != null) {
            // Afficher les données de l'élément sélectionné dans les champs de saisie
            tfmedecin.setText(newSelection.getNommedecin());
            tfpatient.setText(newSelection.getNompatient());
            tfdescription.setText(newSelection.getDescription());
            comboph.setValue(newSelection.getNompharmacie());
            comboph.setDisable(true); // Désactiver le champ pour empêcher la modification
        }
    });}





    @FXML
    void ajouterOrdonnance(ActionEvent event) {
        String nommedecin = tfmedecin.getText();
        String nompatient = tfpatient.getText();
        String description = tfdescription.getText();

        if (!validerSaisies(nommedecin, nompatient, description)) {
            return;
        }

        String insert = "INSERT INTO ordonnance(nommedecin, nompatient, description, pharmacie_id, nompharmacie) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ordonnance = cnx.prepareStatement(insert);
            ordonnance.setString(1, tfmedecin.getText());
            ordonnance.setString(2, tfpatient.getText());
            ordonnance.setString(3, tfdescription.getText());
            ordonnance.setInt(4, 0); // Utilisation de la valeur 0 pour pharmacie_id
            ordonnance.setString(5, comboph.getValue()); // Définir la valeur pour nompharmacie
            ordonnance.executeUpdate();

            showOrdonnance();
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout de l'ordonnance : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getDataO(MouseEvent event) {
        Ordonnance ordonnance = t.getSelectionModel().getSelectedItem();
        id = ordonnance.getId();
        tfmedecin.setText(ordonnance.getNommedecin());
        tfpatient.setText(ordonnance.getNompatient());
        tfdescription.setText(ordonnance.getDescription());
        //tfdate.setDisable(true);
        btnAjouter.setDisable(true);
    }

   /* public void showOrdonnance() {
        ObservableList<Ordonnance> list = getOrdonnances();
        t.setItems(list);
        colMedecin.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("nommedecin"));
        colPatient.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("nompatient"));
        colDescription.setCellValueFactory(new PropertyValueFactory<Ordonnance, String>("Description"));
        //colNompharmacie.setCellValueFactory(new PropertyValueFactory<Ordonnance,String>("pharmacie"));
        colNompharmacie.setCellFactory(param -> new TableCell<Ordonnance, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setGraphic(null);
                } else {
                    comboBox.getItems().addAll(item);
                    comboBox.setValue(item);
                    comboBox.setDisable(true); // Désactiver la ComboBox pour empêcher la modification
                    setGraphic(comboBox);
                }
            }
        });

    }*/
   public void showOrdonnance() {
       ObservableList<Ordonnance> list = getOrdonnances();
       t.setItems(list);
       colMedecin.setCellValueFactory(new PropertyValueFactory<>("nommedecin"));
       colPatient.setCellValueFactory(new PropertyValueFactory<>("nompatient"));
       colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
       colNompharmacie.setCellValueFactory(new PropertyValueFactory<>("nompharmacie"));
   }

    //colDate.setCellValueFactory(new PropertyValueFactory<Ordonnance,Date>("datecreation"));



    public ObservableList<Ordonnance> getOrdonnances() {
        ObservableList<Ordonnance> ordonnances = FXCollections.observableArrayList();
        String query = "select* from ordonnance ";
        cnx = DBConnection.getInstance().getCnx();

        try {
            st = cnx.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Ordonnance ordonnance = new Ordonnance();

                ordonnance.setNommedecin(rs.getString("nommedecin"));
                ordonnance.setNompatient(rs.getString("nompatient"));
                ordonnance.setDescription(rs.getString("Description"));
                ordonnance.setPharmacie_id(rs.getInt("pharmacie_id"));
                ordonnance.setNompharmacie(rs.getString("nompharmacie"));
                //LocalDate dateCreation = rs.getDate("datecreation").toLocalDate();
                //java.sql.Date sqlDate = rs.getDate("datecreation");
                //LocalDate localDate = sqlDate.toLocalDate();
                ordonnance.setId(rs.getInt("Id"));

                ordonnances.add(ordonnance);
                System.out.println(ordonnances);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ordonnances;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void modifierOrd(ActionEvent event) {
        String nommedecin =tfmedecin.getText();
        String nompatient =tfpatient.getText();
        String description =tfdescription.getText();
        if(!validerSaisies(nommedecin,nompatient,description)){return;}

        String update = "update ordonnance set nommedecin = ?, nompatient= ?,description =? where id =?";
        cnx = DBConnection.getInstance().getcnx();
        try {

            PreparedStatement ordonnance = cnx.prepareStatement(update);
            ordonnance.setString(1, tfmedecin.getText());
            ordonnance.setString(2, tfpatient.getText());
            ordonnance.setString(3, tfdescription.getText());
            ordonnance.setInt(4, id);
            ordonnance.executeUpdate();
            showOrdonnance();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    @FXML
    void supprimerord(ActionEvent event) throws SQLException {
        String delete ="delete from ordonnance where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try {
            PreparedStatement pharmacie = cnx.prepareStatement(delete);
            pharmacie.setInt(1,id);
            pharmacie.executeUpdate();
            showOrdonnance();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void clearOrd(ActionEvent event) {clear();

    }
    void clear(){
        tfmedecin.setText(null);
        tfpatient.setText(null);
        tfdescription.setText(null);
        btnAjouter.setDisable(false);
    }

    public boolean validerSaisies(String nommedecin, String nompatient , String description) {
        // Vérifier si l'un des champs est vide
        if (nommedecin.isEmpty() || nompatient.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs.");
            return false; // Retourne false si l'un des champs est vide
        }

        // Validation du nom
        if (nommedecin.length() < 3 || nommedecin.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères et ne doit pas contenir de chiffres.");
            return false; // Retourne false si le nom ne respecte pas les critères
        }

        // Validation de l'adresse
        if (nompatient.length() < 3 || nommedecin.matches(".*\\d.*")){
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères.");
            return false; // Retourne false si l'adresse ne respecte pas les critères
        }

        // Validation du numéro de téléphone
        if (description.length() <5){
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères");
            return false; // Retourne false si le numéro de téléphone ne respecte pas les critères
        }

        return true; // Retourne true si toutes les validations sont passées avec succès
    }



}
