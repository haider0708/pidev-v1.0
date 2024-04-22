/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temlivreure file, choose Tools | Temlivreures
 * and open the temlivreure in the editor.
 */
package com.example.demo4.test;

import java.sql.Date;
import java.sql.SQLException;

import com.example.demo4.services.livreurService;
import com.example.demo4.services.commandeService;


/**
 *
 * @author asus
 */
public class test {
    
      public static void main(String[] args) {   
          
          Date d=Date.valueOf("2022-06-11");
          Date d1=Date.valueOf("2020-04-12");
        try {
            //kifeh ya9ra el orde fel base de donnée , kifeh 3raf nom ev bch n3amarha f nom 

            
            


            commandeService ps=new commandeService();
            //ps.commande(p);
          //  ps.commande(p1);
           // ps.commande(p2);

            //ps.commande(p2);
            System.out.println("");
            livreurService ab = new livreurService();
            //ab.ajouterlivreur(e1);
            //ab.ajouterlivreur(e2);
           // ab.ajouterlivreur(e3);
            //ab.ajouter(p);
            //ab.modifierlivreur(e);
            //ab.supprimerlivreur(e3);
            System.out.println(ab.recupererlivreur());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
