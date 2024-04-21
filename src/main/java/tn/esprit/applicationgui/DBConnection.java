//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tn.esprit.applicationgui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/pidev";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static DBConnection instance;
    private Connection cnx;

    private DBConnection() {
        try {
            this.cnx = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidev", "root", "");
            System.out.println("Connected To DATABASE !");
        } catch (SQLException var2) {
            SQLException e = var2;
            System.err.println("Error: " + e.getMessage());
        }

    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

    public static Connection getCon() {
        return getInstance().getCnx();
    }

    public Connection getCnx() {
        return this.cnx;
    }
}