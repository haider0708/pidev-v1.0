package Controller;
import Model.Patient;
import Service.Service;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class View implements Initializable  {

    @FXML
    private TableView<Patient> PatientTable;

    @FXML
    private TableColumn<Patient, Integer> idcol;

    @FXML
    private TableColumn<Patient, String> emailcol;

    @FXML
    private TableColumn<Patient, String[]> rolescol;

    @FXML
    private TableColumn<Patient, String> passwordcol;

    @FXML
    private TableColumn<Patient, String> firstnamecol;

    @FXML
    private TableColumn<Patient, String> lastnamecol;

    @FXML
    private TableColumn<Patient, String> sexecol;

    @FXML
    private TableColumn<Patient, Integer> agecol;

    @FXML
    private TableColumn<Patient, String> numbercol;

    @FXML
    private TableColumn<Patient, String> imgpathcol;

    @FXML
    private TableColumn<Patient, String> addresscol;

    @FXML
    private TableColumn<Patient, Boolean> isverifiedcol;

    @FXML
    private TableColumn<Patient, String> resettokencol;

    @FXML
    private TableColumn<Patient, String> editCol;

    @FXML
    private Pane pnlOverview;

    private Service Service;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Service = new Service();
        initializeColumns();
        loadData();

    }

    @FXML
    void refresh(MouseEvent event) {
        loadData();
    }



    @FXML
    void getAddView(MouseEvent event) {
        openAddUserView();
    }


    private void initializeColumns() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        rolescol.setCellValueFactory(new PropertyValueFactory<>("roles"));
        passwordcol.setCellValueFactory(new PropertyValueFactory<>("password"));
        firstnamecol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        lastnamecol.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        sexecol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        agecol.setCellValueFactory(new PropertyValueFactory<>("age"));
        numbercol.setCellValueFactory(new PropertyValueFactory<>("number"));
        imgpathcol.setCellValueFactory(new PropertyValueFactory<>("img_path"));
        addresscol.setCellValueFactory(new PropertyValueFactory<>("address"));
        isverifiedcol.setCellValueFactory(new PropertyValueFactory<>("is_verified"));
        resettokencol.setCellValueFactory(new PropertyValueFactory<>("reset_token"));
        editCol.setCellFactory(createEditCellFactory());
    }

    private Callback<TableColumn<Patient, String>, TableCell<Patient, String>> createEditCellFactory() {
        return (TableColumn<Patient, String> param) -> {
            final TableCell<Patient, String> cell = new TableCell<Patient, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setGraphic(createEditIcons());
                        setText(null);
                    }
                }
            };
            return cell;
        };
    }

    private HBox createEditIcons() {
        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

        deleteIcon.setStyle(
                "-fx-cursor: hand ;"
                        + "-glyph-size:28px;"
                        + "-fx-fill:#ff1744;"
        );
        editIcon.setStyle(
                "-fx-cursor: hand ;"
                        + "-glyph-size:28px;"
                        + "-fx-fill:#00E676;"

        );
        deleteIcon.setFill(Paint.valueOf("#ff1744")); // Set the fill color programmatically
        editIcon.setFill(Paint.valueOf("#00E676")); // Set the fill color programmatically


        deleteIcon.setOnMouseClicked(event -> {
            try {
                deletePatient();
            } catch (Exception ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        editIcon.setOnMouseClicked(event -> openEditUserView());

        HBox manageBtn = new HBox(editIcon, deleteIcon);
        manageBtn.setStyle("-fx-alignment:center");
        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));

        return manageBtn;
    }

    private void loadData() {
        try {
            ArrayList<Patient> patientList = Service.afficherAll();
            PatientTable.getItems().setAll(patientList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddUserView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUser.fxml"));
            Pane addUserView = loader.load();
            pnlOverview.getChildren().setAll(addUserView);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void deletePatient() throws SQLException {
        Patient patient = PatientTable.getSelectionModel().getSelectedItem();
        Service.delete(patient.getId());
        loadData();
    }

    private void openEditUserView() {
        Patient patient = PatientTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUser.fxml"));
            Pane addUserView = loader.load();

            AddUser addpatient = loader.getController();
            addpatient.setUpdate(true);
            addpatient.setTextField(
                    patient.getId(),
                    patient.getEmail(),
                    patient.getPassword(),
                    patient.getFirstname(),
                    patient.getLastname(),
                    patient.getAge(),
                    patient.getNumber(),
                    patient.getImg_path(),
                    patient.getAddress()
            );

            pnlOverview.getChildren().setAll(addUserView);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
