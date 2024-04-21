package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Event;
import services.ServiceEvent;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EventController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TextField titreField;
    @FXML
    private TextField localisationField;
    @FXML
    private TextField dateField; // Not used based on provided code, can be removed if DatePicker is used instead
    @FXML
    private Button addButton;

    private ServiceEvent ServiceEvent = new ServiceEvent();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEvents();
        setupTableSelection();
    }

    private void setupTableSelection() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titreField.setText(newSelection.getTitre());
                localisationField.setText(newSelection.getLocalisation());
                datePicker.setValue(LocalDate.parse(newSelection.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
        });
    }

    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();
        String titre = titreField.getText();
        String localisation = localisationField.getText();
        LocalDate date = datePicker.getValue();
        String datePattern = "dd/MM/yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

        if (titre.isEmpty() || titre.length() < 6 || !titre.matches("^[A-Z].*")) {
            errorMessage.append("Title must start with an uppercase letter and be at least 6 characters long.\n");
        }
        if (localisation.isEmpty() || localisation.length() < 3 || !localisation.matches("^[a-zA-Z0-9]{3,}$")) {
            errorMessage.append("Location must be at least 3 characters long and contain only letters and numbers.\n");
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            errorMessage.append("Date cannot be in the past and must be in the format: ").append(datePattern).append(".\n");
        }

        if (errorMessage.length() > 0) {
            showErrorAlert(errorMessage.toString());
            return false;
        }
        return true;
    }


    @FXML
    private void handleAddEvent() {
        if (validateInput()) {
            try {
                String titre = titreField.getText();
                String localisation = localisationField.getText();
                String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Event event = new Event(titre, localisation, date);
                ServiceEvent.addEvent(event);
                loadEvents();  // Refresh table view
                clearFields();
                showSuccessAlert();
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateEvent() {
        if (validateInput()) {
            try {
                Event event = eventTable.getSelectionModel().getSelectedItem();
                event.setTitre(titreField.getText());
                event.setLocalisation(localisationField.getText());
                event.setDate(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                ServiceEvent.updateEvent(event);
                loadEvents();  // Refresh table view
                showSuccessAlert("Event updated successfully!");
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Failed to update event: " + e.getMessage());
            }
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Added");
        alert.setHeaderText(null);
        alert.setContentText("The event has been successfully added.");
        alert.showAndWait();
    }

    private void loadEvents() {
        try {
            List<Event> events = ServiceEvent.getAllEvents();
            eventTable.getItems().setAll(events);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error retrieving data from database: " + e.getMessage());
        }
    }

    private void clearFields() {
        titreField.clear();
        localisationField.clear();
        datePicker.setValue(null);
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                ServiceEvent.deleteEvent(selectedEvent.getId());
                loadEvents();  // Refresh table view
                clearFields();
                showSuccessAlert("Event deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Failed to delete event: " + e.getMessage());
            }
        } else {
            showErrorAlert("No event selected for deletion!");
        }
    }

    public void gotoactivity() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Activity.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Secondary Page");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSwitchToClientView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client.fxml"));
            Parent clientView = loader.load();
            Scene scene = new Scene(clientView);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showErrorAlert("Failed to load client view: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


