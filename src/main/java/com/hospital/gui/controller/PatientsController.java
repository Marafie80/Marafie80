package com.hospital.gui.controller;

import com.hospital.model.Patient;
import com.hospital.service.MedicalRecordService;
import com.hospital.service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PatientsController implements BaseController {
    @FXML private TableView<Patient> patientsTable;
    @FXML private TableColumn<Patient, String> colId;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colLastName;
    @FXML private TableColumn<Patient, LocalDate> colDob;
    @FXML private TableColumn<Patient, String> colGender;
    @FXML private TableColumn<Patient, String> colContact;
    @FXML private TextField searchField;

    private PatientService patientService;
    private MedicalRecordService medicalRecordService;
    private ObservableList<Patient> patients = FXCollections.observableArrayList();

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setMedicalRecordService(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @FXML
    public void initialize() {
        setupTable();
        loadPatients();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colDob.setCellValueFactory(new PropertyValueFactory<>("dob"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colContact.setCellValueFactory(cellData -> {
            String contact = cellData.getValue().getContact() != null ?
                    cellData.getValue().getContact().toString() : "";
            return new javafx.beans.property.SimpleStringProperty(contact);
        });

        patientsTable.setItems(patients);
    }

    private void loadPatients() {
        if (patientService != null) {
            patients.setAll(patientService.findAll());
        }
    }

    @FXML
    private void onSearch() {
        String query = searchField.getText();
        if (patientService != null) {
            patients.setAll(patientService.search(query));
        }
    }

    @FXML
    private void onNewPatient() {
        openPatientForm(null);
    }

    @FXML
    private void onEdit() {
        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a patient to edit");
            return;
        }
        openPatientForm(selected);
    }

    @FXML
    private void onDelete() {
        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a patient to delete");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete Patient");
        confirm.setContentText("Are you sure you want to delete " + selected.getFullName() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                patientService.delete(selected.getId());
                loadPatients();
            }
        });
    }

    @FXML
    private void onViewMedicalRecord() {
        Patient selected = patientsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a patient to view medical record");
            return;
        }
        showAlert("Medical record view for " + selected.getFullName() + " - Feature coming soon!");
    }

    private void openPatientForm(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/patient-form.fxml"));
            Scene scene = new Scene(loader.load());

            PatientFormController controller = loader.getController();
            controller.setPatientService(patientService);
            controller.setMedicalRecordService(medicalRecordService);
            if (patient != null) {
                controller.setPatient(patient);
            }
            controller.setOnSaveCallback(this::loadPatients);
            controller.initialize();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(patient == null ? "New Patient" : "Edit Patient");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error opening patient form: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
