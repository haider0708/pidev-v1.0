package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Event;
import services.ServiceEvent;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


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
    private TextField dateField;
    @FXML
    private Button addButton;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ImageView qrCodeView;

    private ServiceEvent ServiceEvent = new ServiceEvent();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEvents();
        setupTableSelection();
        afficherRepartitionParDate();
    }

    private void setupTableSelection() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    Image qrCode = generateQRCodeImage(newSelection.getTitre() + " - " + newSelection.getLocalisation() + " - " + newSelection.getDate(), 250, 250);
                    qrCodeView.setImage(qrCode);
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String loadHtmlContent(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
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

                // Load HTML content from file
                String htmlContent = loadHtmlContent("src/main/resources/Email.html");

                // Replace placeholders with actual values
                htmlContent = htmlContent.replace("{{eventName}}", titre)
                        .replace("{{eventLocation}}", localisation)
                        .replace("{{eventDate}}", date);

                // Compose invitation email with HTML content
                String inviteSubject = "Invitation to Event: " + titre;

                // Send invitation email
                sendEmail("malekbenslamacontact@gmail.com", inviteSubject, htmlContent);

                loadEvents();
                clearFields();
                showSuccessAlert();
            } catch (SQLException | IOException e) {
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
                loadEvents();
                showSuccessAlert("Event updated successfully!");
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Failed to update event: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                ServiceEvent.deleteEvent(selectedEvent.getId());
                loadEvents();
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

    public void sendEmail(String toEmail, String subject, String body) {
        final String username = "malek.benslama8b3@gmail.com";
        final String password = "hsqjqokkhahloxmb";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    @FXML
    private void handleSendSms() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        String phoneNumber = phoneNumberField.getText();

        if (selectedEvent != null && phoneNumber != null && !phoneNumber.isEmpty()) {
            String message = String.format("Event: %s at %s on %s",
                    selectedEvent.getTitre(), selectedEvent.getLocalisation(), selectedEvent.getDate());
            SmsSender.sendSms(phoneNumber, message);
            showSuccessAlert("SMS sent to " + phoneNumber);
        } else {
            showErrorAlert("Select an event and enter a phone number.");
        }
    }

    public class SmsSender {

        public static final String ACCOUNT_SID = "AC99dc6c7cd0949b302b1e14812ff4e247";
        public static final String AUTH_TOKEN = "bc75308b2170cb7b2ab0a8d1389c9531";
        public static final String FROM_NUMBER = "+12562864029";

        static {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        }

        public static void sendSms(String to, String message) {
            com.twilio.rest.api.v2010.account.Message sms = com.twilio.rest.api.v2010.account.Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(FROM_NUMBER),
                    message
            ).create();

            System.out.println("SMS sent successfully to " + to + " with SID: " + sms.getSid());
        }
    }


    @FXML
    private void handleGenerateStyledPdf() {
        String filePath = "StyledEventsReport.pdf";
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Adding Metadata
            document.addTitle("Rapport des Événements");
            document.addAuthor("Your Company Name");

            // Font specifications
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD, new BaseColor(76, 175, 80));
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new BaseColor(0, 77, 64));
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);

            // Title
            Paragraph title = new Paragraph("Rapport des Événements", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Fetch events
            List<Event> events = ServiceEvent.getAllEvents();
            for (Event event : events) {
                // Section Title for each event
                Chunk sectionTitle = new Chunk(event.getTitre(), sectionFont);
                sectionTitle.setUnderline(0.1f, -2f); // underline
                document.add(new Paragraph(sectionTitle));

                // Details
                document.add(new Paragraph("Localisation: " + event.getLocalisation(), textFont));
                document.add(new Paragraph("Date: " + event.getDate(), textFont));

                // Divider line
                LineSeparator separator = new LineSeparator();
                separator.setPercentage(80);
                separator.setLineColor(new BaseColor(204, 204, 204));
                document.add(new Chunk(separator));
                document.add(Chunk.NEWLINE); // Spacing
            }

            document.close();

            // Opening the document
            Desktop.getDesktop().open(new File(filePath));
            showSuccessAlert("PDF stylisé généré et ouvert avec succès!");
        } catch (DocumentException | IOException | SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }



    @FXML
    private PieChart pieChart;

    private void afficherRepartitionParDate() {
        ObservableList<Event> events = eventTable.getItems();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Using a Map to count events by date
        Map<String, Integer> dateCount = new HashMap<>();

        for (Event event : events) {
            dateCount.merge(event.getDate(), 1, Integer::sum);
        }

        // Convert counts to PieChart data
        dateCount.forEach((date, count) -> {
            pieChartData.add(new PieChart.Data(date, count));
        });

        // Update the pie chart
        pieChart.setData(pieChartData);

        // Apply CSS styling
        pieChart.setStyle("-fx-background-color: white;"); // Set white background

        // Additional styling (optional)
        for (PieChart.Data data : pieChartData) {
            Node node = data.getNode();
            node.setStyle("-fx-pie-color: #somecolor;"); // Set color for each section
        }
    }

    private Image generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        Image image = new Image(new ByteArrayInputStream(pngData));
        System.out.println("QR Code Generated: " + (image != null));
        return image;
    }

}


