package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/projet";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static DBConnection instance;
    private Connection cnx;

    // Make the constructor private to prevent instantiation from outside
    private DBConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database!");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            // Handle exception appropriately, such as logging or throwing a runtime exception
            throw new RuntimeException("Failed to connect to the database.", e);
        }
    }

    // Make getInstance() method synchronized for thread safety
    public static synchronized DBConnection getInstance() {
        if (instance == null)
            instance = new DBConnection();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }
}