package com.hospital.gui.controller;

import com.hospital.model.Appointment;
import com.hospital.model.AppointmentStatus;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class AppointmentsController implements BaseController {
    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableColumn<Appointment, String> colId;
    @FXML private TableColumn<Appointment, String> colPatient;
    @FXML private TableColumn<Appointment, String> colStaff;
    @FXML private TableColumn<Appointment, LocalDateTime> colStart;
    @FXML private TableColumn<Appointment, LocalDateTime> colEnd;
    @FXML private TableColumn<Appointment, AppointmentStatus> colStatus;
    @FXML private ComboBox<String> filterComboBox;

    private AppointmentService appointmentService;
    private PatientService patientService;
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    @FXML
    public void initialize() {
        setupTable();
        setupFilter();
        loadAppointments();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPatientId()));
        colStaff.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStaffId()));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointmentsTable.setItems(appointments);
    }

    private void setupFilter() {
        filterComboBox.getItems().addAll("All", "Scheduled", "Completed", "Cancelled");
        filterComboBox.setValue("All");
        filterComboBox.setOnAction(e -> loadAppointments());
    }

    private void loadAppointments() {
        if (appointmentService != null) {
            appointments.setAll(appointmentService.findAll());
            applyFilter();
        }
    }

    private void applyFilter() {
        String filter = filterComboBox.getValue();
        if (filter != null && !filter.equals("All")) {
            AppointmentStatus status = AppointmentStatus.valueOf(filter.toUpperCase());
            appointments.setAll(appointmentService.findAll().stream()
                    .filter(a -> a.getStatus() == status)
                    .toList());
        }
    }

    @FXML
    private void onNewAppointment() {
        showAlert("New appointment form - Feature coming soon!");
    }

    @FXML
    private void onEdit() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an appointment to edit");
            return;
        }
        showAlert("Edit appointment - Feature coming soon!");
    }

    @FXML
    private void onComplete() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an appointment to complete");
            return;
        }

        try {
            appointmentService.complete(selected.getId());
            loadAppointments();
            showAlert("Appointment marked as completed");
        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        Appointment selected = appointmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an appointment to cancel");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Cancel");
        confirm.setHeaderText("Cancel Appointment");
        confirm.setContentText("Are you sure you want to cancel this appointment?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    appointmentService.cancel(selected.getId());
                    loadAppointments();
                } catch (Exception e) {
                    showAlert("Error: " + e.getMessage());
                }
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
