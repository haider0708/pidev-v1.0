module devlab.user {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;

    opens Test to javafx.fxml;
    exports Test;
    opens Controller to javafx.fxml;
    exports Controller;
    opens Model to javafx.fxml;
    exports Model;
    opens Service to javafx.fxml;
    exports Service;
    opens Utils to javafx.fxml;
    exports Utils;


}