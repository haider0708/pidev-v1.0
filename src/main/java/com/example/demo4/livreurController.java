/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.commande;
import com.example.demo4.entities.livreur;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import com.example.demo4.services.commandeService;

/**
 * FXML Controller class
 *
 * @author asus
 */

public class livreurController implements Initializable {

    int idev;


    @FXML
    private Label nomevLabel;
    @FXML
    private Label prenomevLabel;



    @FXML
    private Label numero_telLabel;



    commandeService Ps = new commandeService();
    @FXML
    private TextField idevF;
    @FXML
    private TextField iduserF;


    @FXML
    private ImageView imageview;

    @FXML
    private TextField idPartField;



    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {



        idevF.setVisible(false);
    }

    private livreur eve = new livreur();

    public void setlivreur(livreur e) {
        this.eve = e;

        nomevLabel.setText(e.getNom());
        prenomevLabel.setText(e.getPrenom());

        idevF.setText(String.valueOf(e.getId()));
        iduserF.setText(String.valueOf(1));
        String path = e.getImage();
        File file = new File(path);
        Image img = new Image(file.toURI().toString());
        imageview.setImage(img);


        numero_telLabel.setText(String.valueOf(e.getNumero_tel()));

    }

    public void setIdev(int idev) {
        this.idev = idev;
    }

    @FXML
    private void commanderev(MouseEvent ev) throws SQLException {
        commande p = new commande( Integer.parseInt(idevF.getText()));
        Ps.ajoutercommande(p);
        idPartField.setText(String.valueOf(27));


        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("affichercommande.fxml")));
            idevF.getScene().setRoot(loader);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }




}
