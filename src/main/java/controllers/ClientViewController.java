package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Activity;
import models.Event;
import services.ServiceActivity;
import services.ServiceEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ClientViewController implements Initializable {

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private TableColumn<Event, String> colTitle;

    @FXML
    private TableColumn<Event, String> colDate;

    @FXML
    private TableColumn<Event, String> colLocation;

    @FXML
    private ListView<String> activityListView;

    private ServiceEvent serviceEvent;
    private ServiceActivity serviceActivity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serviceEvent = new ServiceEvent();
        serviceActivity = new ServiceActivity();

        colTitle.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        loadEventData();

        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadActivitiesForEvent(newSelection.getId());
            }
        });
    }

    private void loadEventData() {
        try {
            ObservableList<Event> data = FXCollections.observableArrayList(serviceEvent.getAllEvents());
            eventTable.setItems(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadActivitiesForEvent(int eventId) {
        try {
            List<Activity> activities = serviceActivity.getActivitiesByEventId(eventId);
            ObservableList<String> activityDetails = FXCollections.observableArrayList();
            for (Activity activity : activities) {
                String displayText = String.format("%s - %s, (Organized by: %s, Status: %s)",
                        activity.getNom(),
                        activity.getDescription(),
                        activity.getOrganisateur(),
                        activity.getStatus());
                activityDetails.add(displayText);
            }
            activityListView.setItems(activityDetails);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load activities", "Unable to load activities for the selected event due to a database error.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) eventTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot navigate back", "Failed to load the initial view.");
        }
    }
    @FXML
    private void handleChatbotButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Chatbot.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) eventTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot open chatbot", "Failed to load the chatbot view.");
        }
    }

}