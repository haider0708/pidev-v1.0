package test;

import models.Pharmacie;
import services.ServicePharmacie;
import utils.DBConnection;

import java.sql.SQLException;

public class MainClass {
    public static void main(String[] args) {
        // Création de la connexion à la base de données
        DBConnection cn1 = DBConnection.getInstance();

        // Création d'une instance de Pharmacie
        Pharmacie pharmacie = new Pharmacie("pharmacie saadani", "mannouba", 52715000);

        // Création d'une instance de ServicePharmacie
        ServicePharmacie servicePharmacie = new ServicePharmacie();

        try {
            // Insertion d'une pharmacie
            servicePharmacie.insertOne(pharmacie);

            // Affichage de toutes les pharmacies
            System.out.println(servicePharmacie.selectAll());
            //suppression
           // Pharmacie pharmacieASupprimer = new Pharmacie();
           // pharmacieASupprimer.setNom("pharmacie kenoun");
           // servicePharmacie.deleteOne(pharmacieASupprimer);
           // System.out.println("Pharmacie supprimée avec succès !");
            //modification
            // Modification d'une pharmacie
            //Pharmacie pharmacieAModifier = new Pharmacie("pharmacie kenoun", "mannnouba", 123456789);
           // servicePharmacie.updateOne(pharmacieAModifier);
            //System.out.println("Pharmacie mise à jour avec succès !");

            // Affichage de toutes les pharmacies après la modification
            System.out.println(servicePharmacie.selectAll());
        } catch (SQLException e) {
            // Gestion des exceptions
            System.err.println("Erreur: " + e.getMessage());
        }
    }


}
