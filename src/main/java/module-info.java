module tn.esprit.applicationgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;

    opens tn.esprit.applicationgui to javafx.fxml;
    exports tn.esprit.applicationgui;
}