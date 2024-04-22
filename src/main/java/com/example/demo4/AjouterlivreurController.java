/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.livreur;
import com.example.demo4.services.livreurService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AjouterlivreurController implements Initializable {

    @FXML
    private TextField nomevField;
    @FXML
    private TextField prenomevField;
    @FXML
    private TextField numerotelField;




    @FXML
    private TextField imageevField;


    @FXML
    private TableView<livreur> livreurTv;


    @FXML
    private TableColumn<livreur, String> imageevTv;

    @FXML
    private TableColumn<livreur, String> nomevTv;
    @FXML
    private TableColumn<livreur, String> prenomevTv;
    @FXML
    private TableColumn<livreur, Integer> numerotelTv;





    livreurService Ev = new livreurService();


    @FXML
    private TextField idmodifierField;

    @FXML
    private ImageView imageview;
    @FXML
    private TextField rechercher;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        getevs();
        // affichage tableau


    }



    @FXML
    private void ajouterlivreur(ActionEvent ev) {
        int part = 0;
        if (imageevField.getText().isEmpty() || nomevField.getText().isEmpty() || prenomevField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Fields cannot be empty");
            alert.showAndWait();
        } else if (!numerotelField.getText().matches("\\d{8}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Please enter a valid Tunisian phone number (8 digits)");
            alert.showAndWait();
        } else if (numerotelField.getText().matches(".*(\\d)\\1{6,}.*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText("Error!");
            alert.setContentText("Please enter a phone number with at most 7 consecutive identical digits");
            alert.showAndWait();
        } else {
            livreur e = new livreur();

            e.setNom(nomevField.getText());
            e.setPrenom(prenomevField.getText());
            e.setNumero_tel(Integer.parseInt(numerotelField.getText()));
            e.setImage(imageevField.getText());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information ");
            alert.setHeaderText("livreur add");
            alert.setContentText("livreur added successfully!");
            alert.showAndWait();

            try {
                Ev.ajouterlivreur(e);
                reset();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            getevs();
        }
    }



    private void reset() {

        nomevField.setText("");
        prenomevField.setText("");
        numerotelField.setText("");

        imageevField.setText("");

    }

    public void getevs() {
        try {
            List<livreur> livreur = Ev.recupererlivreur();
            ObservableList<livreur> olp = FXCollections.observableArrayList(livreur);
            livreurTv.setItems(olp);

            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image"));

            nomevTv.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomevTv.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            numerotelTv.setCellValueFactory(new PropertyValueFactory<>("numerotel"));

        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }

    @FXML
    private void modifierlivreur(ActionEvent ev) throws SQLException {
        livreur e = new livreur();
        e.setId(Integer.parseInt(idmodifierField.getText()));

        e.setNom(nomevField.getText());
        e.setPrenom(prenomevField.getText());
        e.setNumero_tel(Integer.parseInt(numerotelField.getText()));



        e.setImage(imageevField.getText());
        Ev.modifierlivreur(e);
        reset();
        getevs();
    }

    @FXML
    private void supprimerlivreur(ActionEvent ev) {
        livreur e = livreurTv.getItems().get(livreurTv.getSelectionModel().getSelectedIndex());
            try {
            Ev.supprimerlivreur(e);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterlivreurController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("livreur delete");
        alert.setContentText("livreur deleted successfully!");
        alert.showAndWait();
        getevs();
    }

    @FXML
    private void afficherlivreur(ActionEvent ev) {
        try {
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("afficherlivreur.fxml")));
            livreurTv.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void choisirev(MouseEvent ev) throws IOException {
        livreur e = livreurTv.getItems().get(livreurTv.getSelectionModel().getSelectedIndex());
        idmodifierField.setText(String.valueOf(e.getId()));

        imageevField.setText(e.getImage());
        nomevField.setText(e.getNom());
        prenomevField.setText(e.getPrenom());
        numerotelField.setText(String.valueOf(e.getNumero_tel()));

        String path = e.getImage();
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        imageview.setImage(img);
    }

    @FXML
    private void uploadImage(ActionEvent ev) throws FileNotFoundException, IOException {
        Random rand = new Random();
        int x = rand.nextInt(1000);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);
        String DBPath = "C:\\\\xampp\\\\htdocs\\\\imageP\\\\" + x + ".jpg";
        if (file != null) {
            FileInputStream Fsource = new FileInputStream(file.getAbsolutePath());
            FileOutputStream Fdestination = new FileOutputStream(DBPath);
            BufferedInputStream bin = new BufferedInputStream(Fsource);
            BufferedOutputStream bou = new BufferedOutputStream(Fdestination);
            String path = file.getAbsolutePath();
            Image img = new Image(file.toURI().toString());
            imageview.setImage(img);
            imageevField.setText(DBPath);
            int b = 0;
            while (b != -1) {
                b = bin.read();
                bou.write(b);
            }
            bin.close();
            bou.close();
        } else {
            System.out.println("error");
        }
    }

    @FXML
    private void rechercherev(KeyEvent ev) {
        livreurService bs = new livreurService();
        ObservableList<livreur> filter = bs.chercherev(rechercher.getText());
        populateTable(filter);
    }

    private void populateTable(ObservableList<livreur> branlist) {
        livreurTv.setItems(branlist);
    }
}
