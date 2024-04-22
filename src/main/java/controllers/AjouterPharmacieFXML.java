package controllers;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Pharmacie;
import services.ServicePharmacie;
import utils.DBConnection;
import javax.swing.*;
import javafx.scene.control.TextFormatter;//controle de saisie numero
import javafx.util.StringConverter;
public class AjouterPharmacieFXML implements Initializable {
    public Button btnAjout;
    Connection cnx = null;
    PreparedStatement st =null;
    ResultSet rs = null;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfAdresse;

    @FXML
    private TextField tfNumero;

    @FXML
    private TableColumn<Pharmacie, String> colAdresse;

    @FXML
    private TableColumn<Pharmacie, Integer> colId;
    @FXML
    private TableColumn<Pharmacie, String> colNom;
    @FXML
    private TableColumn<Pharmacie, Integer> colTelephone;
    @FXML
    private TableView<Pharmacie> table;
int id =0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showPharmacie();
        //condition de stopper l utilisateur s il depasse 8 chiffres
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= 8) {
                return change;
            } else {
                return null;
            }
        });

        // Appliquer le TextFormatter au champ de saisie du numéro de téléphone
        tfNumero.setTextFormatter(textFormatter);
    }
    public ObservableList<Pharmacie>getPharmacies(){
        ObservableList<Pharmacie> pharmacies = FXCollections.observableArrayList();
        String query = "select* from pharmacie ";
        cnx = DBConnection.getInstance().getCnx();
        try {
            st = cnx.prepareStatement(query);
            rs = st.executeQuery();
    while(rs.next()){
    Pharmacie pharmacie = new Pharmacie();
    pharmacie.setId(rs.getInt("Id"));
    pharmacie.setNom(rs.getString("Nom"));
    pharmacie.setAdresse(rs.getString("Adresse"));
    pharmacie.setNumerotelephone(rs.getInt("NumeroTelephone"));
    pharmacies.add(pharmacie);
}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pharmacies;
    }
    //affichage tableau
    public void showPharmacie()
    {
        ObservableList<Pharmacie> list = getPharmacies();
        table.setItems(list);
        //colId.setCellValueFactory(new PropertyValueFactory<Pharmacie,Integer>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<Pharmacie,String>("Nom"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<Pharmacie,String>("Adresse"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<Pharmacie,Integer>("numeroTelephone"));
    }
        @FXML
    void ajouterPharmacie(ActionEvent event) {
       /* try {
            Pharmacie pharmacie = new Pharmacie(tfNom.getText(), tfAdresse.getText(), Integer.parseInt(tfNumero.getText()));
            ServicePharmacie servicePharmacie = new ServicePharmacie();
            servicePharmacie.insertOne(pharmacie);
            showAlert(Alert.AlertType.INFORMATION, "Pharmacie ajoutée", "La pharmacie a été ajoutée avec succès.");
            showPharmacie();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Une erreur s'est produite lors de l'insertion de la pharmacie.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un numéro de téléphone valide.");
        }*/
            String nom = tfNom.getText();
            String adresse = tfAdresse.getText();
            String numeroTelephone = tfNumero.getText();

            if (!validateInput(nom, adresse, numeroTelephone)) {
                return;
            }
            String insert = "insert into pharmacie(Nom,Adresse,NumeroTelephone) values(?,?,?)";
            cnx=DBConnection.getInstance().getCnx();
            try{
                PreparedStatement pharmacie = cnx.prepareStatement(insert);
                pharmacie.setString(1,tfNom.getText());
                pharmacie.setString(2,tfAdresse.getText());
                pharmacie.setInt(3, Integer.parseInt(tfNumero.getText()));
                pharmacie.executeUpdate();
                clear();
                showPharmacie();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    @FXML
    void getData(MouseEvent event) {
        Pharmacie pharmacie= table.getSelectionModel().getSelectedItem();
        id = pharmacie.getId();
        tfNom.setText(pharmacie.getNom());
        tfAdresse.setText(pharmacie.getAdresse());
        tfNumero.setText(String.valueOf(pharmacie.getNumeroTelephone()));
        btnAjout.setDisable(true);

    }
    void clear(){
        tfNom.setText(null);
        tfNumero.setText(null);
        tfAdresse.setText(null);
        btnAjout.setDisable(false);
    }
    @FXML
    void MettreaJour(ActionEvent event) throws SQLException{
        String nom = tfNom.getText();
        String adresse = tfAdresse.getText();
        String numeroTelephone = tfNumero.getText();

        if (!validateInput(nom, adresse, numeroTelephone)) {
            return;
        }
        String update = "update pharmacie set nom = ?, adresse= ?,numerotelephone =? where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try{
            PreparedStatement pharmacie = cnx.prepareStatement(update);
            pharmacie.setString(1,tfNom.getText());
            pharmacie.setString(2,tfAdresse.getText());
            pharmacie.setString(3,tfNumero.getText());
            pharmacie.setInt(4,id);
            pharmacie.executeUpdate();
            showPharmacie();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void supprimerPharmacie(ActionEvent event) throws SQLException {
        String delete ="delete from pharmacie where id =?";
        cnx =DBConnection.getInstance().getCnx();
        try {
            PreparedStatement pharmacie = cnx.prepareStatement(delete);
            pharmacie.setInt(1,id);
            pharmacie.executeUpdate();
            showPharmacie();
            clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void clearField(ActionEvent event) {
clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
    private boolean validateInput(String nom, String adresse, String numeroTelephone) {
        if (nom.isEmpty() || adresse.isEmpty() || numeroTelephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez remplir tous les champs.");
            return false;
        }

        // Validation du nom
        if (nom.length() < 3 || nom.matches(".*\\d.*")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères et ne doit pas contenir de chiffres.");
            return false;
        }

        // Validation de l'adresse
        if (adresse.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "L'adresse doit contenir au moins 3 caractères.");
            return false;
        }

        // Validation du numéro de téléphone
        if (numeroTelephone.length() != 8 || !numeroTelephone.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return false;
        }

        return true;
    }
}
