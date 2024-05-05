package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class Maps {
    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        System.out.println("Initializing Maps Controller...");
        Platform.runLater(() -> {
            try {
                String url = getClass().getResource("/Maps.html").toExternalForm();
                System.out.println("Loading URL: " + url);
                webView.getEngine().load(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        webView.getEngine().setOnAlert(event -> System.out.println(event.getData()));
        webView.getEngine().getLoadWorker().exceptionProperty().addListener((o, old, t) -> {
            if (t != null) {
                t.printStackTrace();
            }
        });
    }
}

