module tn.esprit.applicationgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires java.sql;
    requires org.apache.pdfbox;
    requires twilio;
    requires org.controlsfx.controls;

    requires java.desktop;

    opens tn.esprit.applicationgui to javafx.fxml;
    exports tn.esprit.applicationgui;
}