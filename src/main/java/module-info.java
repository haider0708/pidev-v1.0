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
    requires jbcrypt;
    requires com.google.gson;
    requires jdk.httpserver;
    requires com.google.api.client;
    requires google.api.client;
    requires com.google.api.client.auth;
    requires com.google.api.client.extensions.java6.auth;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.services.gmail;
    requires com.google.api.client.json.gson;
    requires org.apache.commons.codec;
    requires mail;
    requires infobip.api.java.client;
    requires okhttp3;
    requires TrayTester;

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