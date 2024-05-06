/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package com.example.demo4;

import com.example.demo4.entities.commande;
import com.example.demo4.entities.livreur;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
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
import org.controlsfx.control.Rating;

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
    private TextField nom_clientevField;
    @FXML
    private TextField addresse_clientevField;
    @FXML
    private TextField numero_clientevField;


    @FXML
    private Label numero_telLabel;

    @FXML
    private Rating rating;

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
    private final String ACCOUNT_SID = "AC37fe287f749f857986973c4729c39662";
    private final String AUTH_TOKEN = "a73e44c32e7b94fdc908eea20d24ecfa";
    private final String TWILIO_PHONE_NUMBER = "+13342316049";
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
        double ratingValue = rating.getRating();
        commande p = new commande( Integer.parseInt(idevF.getText()), nom_clientevField.getText(), addresse_clientevField.getText(), numero_clientevField.getText(), ratingValue);
        Ps.ajoutercommande(p);
        idPartField.setText(String.valueOf(27));

        // Envoi du SMS
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber("+21628860682"),  // Numéro de téléphone du destinataire
                new PhoneNumber(TWILIO_PHONE_NUMBER),   // Numéro Twilio
                "Vous avez commander!"
        ).create();

        System.out.println("SMS envoyé avec succès! SID: " + message.getSid());
        resetPart();

    }
    public void resetPart() {
        nom_clientevField.setText("");
        addresse_clientevField.setText("");
        numero_clientevField.setText("");



    }




}
