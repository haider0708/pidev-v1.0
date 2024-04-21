package utils;
import java.sql.*;

public class DBConnection {
    final String URL = "jdbc:mysql://localhost:3306/event";
    final String USER ="root";
    final String PASS= "";
    private Connection connection;
    private static DBConnection instance;

    public DBConnection(){
        try {
            connection = DriverManager.getConnection(URL, USER ,PASS);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection(){
        return connection;
    }
}