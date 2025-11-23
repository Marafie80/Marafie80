package com.hospital.gui.controller;

import com.hospital.model.ContactInfo;
import com.hospital.model.Patient;
import com.hospital.service.MedicalRecordService;
import com.hospital.service.PatientService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PatientFormController implements BaseController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private DatePicker dobPicker;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextArea addressArea;
    @FXML private Label errorLabel;

    private PatientService patientService;
    private MedicalRecordService medicalRecordService;
    private Patient patient;
    private Runnable onSaveCallback;

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setMedicalRecordService(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");
        genderComboBox.getItems().addAll("Male", "Female", "Other");

        if (patient != null) {
            loadPatientData();
        }
    }

    private void loadPatientData() {
        firstNameField.setText(patient.getFirstName());
        lastNameField.setText(patient.getLastName());
        dobPicker.setValue(patient.getDob());
        genderComboBox.setValue(patient.getGender());

        if (patient.getContact() != null) {
            phoneField.setText(patient.getContact().getPhone());
            emailField.setText(patient.getContact().getEmail());
            addressArea.setText(patient.getContact().getAddress());
        }
    }

    @FXML
    private void onSave() {
        try {
            if (!validateInput()) {
                return;
            }

            if (patient == null) {
                patient = new Patient();
            }

            patient.setFirstName(firstNameField.getText().trim());
            patient.setLastName(lastNameField.getText().trim());
            patient.setDob(dobPicker.getValue());
            patient.setGender(genderComboBox.getValue());

            ContactInfo contact = new ContactInfo();
            contact.setPhone(phoneField.getText().trim());
            contact.setEmail(emailField.getText().trim());
            contact.setAddress(addressArea.getText().trim());
            patient.setContact(contact);

            boolean isNew = patient.getId() == null;
            Patient saved = patient.getId() == null ?
                    patientService.create(patient) :
                    patientService.update(patient);

            // Create medical record for new patients
            if (isNew && medicalRecordService != null) {
                try {
                    medicalRecordService.createRecord(saved.getId());
                } catch (Exception e) {
                    System.err.println("Warning: Could not create medical record: " + e.getMessage());
                }
            }

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            closeWindow();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private boolean validateInput() {
        if (firstNameField.getText().trim().isEmpty()) {
            errorLabel.setText("First name is required");
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            errorLabel.setText("Last name is required");
            return false;
        }
        if (dobPicker.getValue() == null) {
            errorLabel.setText("Date of birth is required");
            return false;
        }
        if (dobPicker.getValue().isAfter(LocalDate.now())) {
            errorLabel.setText("Date of birth cannot be in the future");
            return false;
        }
        if (genderComboBox.getValue() == null) {
            errorLabel.setText("Gender is required");
            return false;
        }
        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }
}
