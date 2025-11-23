package com.hospital.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AppointmentFormController implements BaseController {
    @FXML private ComboBox<String> patientComboBox;
    @FXML private ComboBox<String> doctorComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private TextArea notesArea;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        errorLabel.setText("");
        setupSpinners();
    }

    private void setupSpinners() {
        startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        startMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 10));
        endMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 15));
    }

    @FXML
    private void onSchedule() {
        errorLabel.setText("Appointment scheduling - Feature coming soon!");
    }

    @FXML
    private void onCancel() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close();
    }
}
