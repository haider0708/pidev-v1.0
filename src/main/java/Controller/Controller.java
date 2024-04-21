package Controller;

import Model.Patient;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import Service.Service;

public class Controller implements Initializable {

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

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
    private Pane pnlCustomer;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private VBox pnItems = null;

    private Service Service;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Service = new Service();
        initializeColumns();
        loadData();
    }

    @FXML
    void refresh(MouseEvent event) {
        loadData();
    }

    @FXML
    void Close(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void getAddView(MouseEvent event) {
        openAddUserView();
    }

    @FXML
    void handleClicks(ActionEvent event) {
        // Handle button clicks if necessary
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
            ArrayList<Model.Patient> patientList = Service.afficherAll();
            PatientTable.getItems().setAll(patientList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openAddUserView() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/AddUser.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
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
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddUser.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
}
