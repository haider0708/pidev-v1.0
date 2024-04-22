package test;

import models.Produit;
import services.ServiceProduit;
import utils.DBConnection;

import java.sql.SQLException; // Import SQLException from java.sql package

public class MainClass {
    public static void main(String[] args) {
        DBConnection cn1 = DBConnection.getInstance();
        //Produit p = new Produit("Ben", 12, "beh","beh");

        ServiceProduit serviceProduit = new ServiceProduit();

        try {
           //serviceProduit.insertOne(p);
            //suppression
             /*Produit produitASupprimer = new Produit();
            produitASupprimer.setNom("Produit saadani");
            serviceProduit.deleteOne(produitASupprimer);
             System.out.println("Pharmacie supprimée avec succès !");*/
            System.out.println(serviceProduit.selectAll());
            //modification
           // Produit produitAModifier = new Produit("Ben", 25, "bonne qualite");
            //serviceProduit.updateOne(produitAModifier);
            //System.out.println("Produit mise à jour avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
    }
}



