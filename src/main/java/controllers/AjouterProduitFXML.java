package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import utils.DBConnection;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import models.Produit;
import services.ServiceProduit;
import java.util.ArrayList;
import java.util.List;
    public class AjouterProduitFXML implements Initializable {
        public Button btnajouter;
        Connection cnx = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        @FXML
        private TextField tfDescription;
        @FXML
        private TableColumn<Produit, String> colnomcategorie;
        @FXML
        private ComboBox<String> combop;
        @FXML
        private TextField tfNom;

        @FXML
        private TextField tfPrix;
        @FXML

        private TableColumn<Produit, String> colDescription;

        @FXML
        private TableColumn<Produit, Integer> colId;

        @FXML
        private TableColumn<Produit, String> colNom;

        @FXML
        private TableColumn<Produit, Integer> colPrix;

        @FXML
        private TableView<Produit> table;
        int id = 0;

       /* @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
            try {
                String sql = "SELECT DISTINCT nomcategorie FROM categorie";
                PreparedStatement statement = cnx.prepareStatement(sql);

                ResultSet rs = statement.executeQuery();
                List<String> produits = new ArrayList<>();
                while (rs.next()) {
                    produits.add(rs.getString("nomcategorie"));
                }
                ObservableList<String> options = FXCollections.observableArrayList(produits);
                combop.setItems(options);
            } catch (
                    SQLException e) {
                e.printStackTrace();  // Gérer l'exception selon vos besoins
            }

            showProduit();
            table.getSelectionModel().

                    selectedItemProperty().

                    addListener((obs, oldSelection, newSelection) ->

                    {
                        if (newSelection != null) {
                            // Afficher les données de l'élément sélectionné dans les champs de saisie
                            tfNom.setText(newSelection.getNom());
                            tfPrix.setText(String.valueOf(newSelection.getPrix()));
                            tfDescription.setText(newSelection.getDescription());
                            combop.setValue(newSelection.getNomcategorie());
                            combop.setDisable(true); // Désactiver le champ pour empêcher la modification
                        }
                    });
        }*/
       @Override
       public void initialize(URL url, ResourceBundle resourceBundle) {
           cnx = DBConnection.getInstance().getCnx(); // Initialize cnx here

           try {
               String sql = "SELECT DISTINCT nomcategorie FROM categorie";
               PreparedStatement statement = cnx.prepareStatement(sql);

               ResultSet rs = statement.executeQuery();
               List<String> produits = new ArrayList<>();
               while (rs.next()) {
                   produits.add(rs.getString("nomcategorie"));
               }
               ObservableList<String> options = FXCollections.observableArrayList(produits);
               combop.setItems(options);
           } catch (SQLException e) {
               e.printStackTrace();  // Handle the exception according to your needs
           }

           showProduit();
           table.getSelectionModel()
                   .selectedItemProperty()
                   .addListener((obs, oldSelection, newSelection) -> {
                       if (newSelection != null) {
                           // Display data of the selected item in the input fields
                           tfNom.setText(newSelection.getNom());
                           tfPrix.setText(String.valueOf(newSelection.getPrix()));
                           tfDescription.setText(newSelection.getDescription());
                           combop.setValue(newSelection.getNomcategorie());
                           combop.setDisable(true); // Disable the field to prevent modification
                       }
                   });
       }


        public ObservableList<Produit> getproduits(){
            ObservableList<Produit> produits = FXCollections.observableArrayList();
            String query = "select * from produit ";
            cnx = DBConnection.getInstance().getCnx();

            try {
                st = cnx.prepareStatement(query);
                rs = st.executeQuery();
                while(rs.next()){
                    Produit produit = new Produit();
                    produit.setId(rs.getInt("Id"));
                    produit.setNom(rs.getString("Nom"));
                    produit.setPrix(rs.getInt("Prix"));
                    produit.setDescription(rs.getString("Description"));
                    produit.setNomcategorie(rs.getString("nomcategorie")); // Ensure this line is present
                    System.out.println("Nom Catégorie: " + produit.getNomcategorie()); // Debug output
                    produits.add(produit);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return produits;
        }

        public void showProduit()
        {
            ObservableList<Produit> list = getproduits();
            table.setItems(list);
           // colId.setCellValueFactory(new PropertyValueFactory<Produit,Integer>("id"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
            colPrix.setCellValueFactory(new PropertyValueFactory<>("Prix"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));

            colnomcategorie.setCellValueFactory(new PropertyValueFactory<>("nomcategorie"));

        }

        @FXML
        void ajouterProduit(ActionEvent event) {

            String nom = tfNom.getText();
            String prixText = tfPrix.getText();
            String description = tfDescription.getText();

            if (!validateInput(nom, prixText, description)) {
                return; // Exit the method if the data is not valid
            }
            String insert = "insert into produit(Nom,Prix,Description,categorie_id,nomcategorie) values(?,?,?,?,?)";
            cnx=DBConnection.getInstance().getCnx();
            try{
                PreparedStatement produit = cnx.prepareStatement(insert);
                produit.setString(1,tfNom.getText());
                produit.setInt(2,Integer.parseInt(tfPrix.getText()));
                produit.setString(3,tfDescription.getText());
                produit.setInt(4,0);
                produit.setString(5,combop.getValue());
                produit.executeUpdate();
                clear();
                showProduit();
                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "Le produit a été ajoutée avec succès.");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de l'ajout de l'ordonnance : " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        @FXML
        void getData(MouseEvent event) {

            Produit produit= table.getSelectionModel().getSelectedItem();
            id = produit.getId();
            tfNom.setText(produit.getNom());
            tfPrix.setText(String.valueOf(produit.getPrix()));
            tfDescription.setText(produit.getDescription());

            btnajouter.setDisable(true);

        }
        void clear(){
            tfNom.setText(null);
            tfPrix.setText(null);
            tfDescription.setText(null);
            btnajouter.setDisable(false);
        }
        @FXML
        void Modifier(ActionEvent event)  throws SQLException{
            String nom = tfNom.getText();
            String prixText = tfPrix.getText();
            String description = tfDescription.getText();

            if (!validateInput(nom, prixText, description)) {
                return; // Sortie de la méthode si les données ne sont pas valides
            }
                String update = "update produit set nom = ?, prix= ?,description =? where id =?";
                cnx =DBConnection.getInstance().getCnx();
                try{
                    PreparedStatement produit = cnx.prepareStatement(update);
                    produit.setString(1,tfNom.getText());
                    produit.setString(2,tfPrix.getText());
                    produit.setString(3,tfDescription.getText());
                    produit.setInt(4,id);
                    produit.executeUpdate();
                    showProduit();
                    showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Le produit a été modifiée avec succès.");
                    clear();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        }

        @FXML
        void Supprimer(ActionEvent event) throws SQLException {
            String delete ="delete from produit where id =?";
            cnx =DBConnection.getInstance().getCnx();
            try {
                PreparedStatement produit = cnx.prepareStatement(delete);
                produit.setInt(1,id);
                produit.executeUpdate();
                showProduit();
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussi", "Le produit a été supprimer avec succès.");
                clear();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        @FXML
        void clearFieled(ActionEvent event) {
         clear();

        }

        private void showAlert(Alert.AlertType type, String title, String content) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setContentText(content);
            alert.show();
        }
        private boolean validateInput(String nom, String prixText, String description) {
            if (nom.isEmpty() || prixText.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Tous les champs doivent être remplis.");
                return false;
            }
            if (nom.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le nom doit contenir au moins 3 caractères.");
                return false;
            }

            try {
                int prix = Integer.parseInt(prixText);
                if (prix < 0 || prix > 999) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Le prix doit être entre 0 et 999.");
                    return false;
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "Veuillez saisir un prix valide (nombre entier).");
                return false;
            }

            if (description.length() < 5) {
                showAlert(Alert.AlertType.ERROR, "Erreur de saisie", "La description doit contenir au moins 5 caractères.");
                return false;
            }

            return true;
        }
    }


