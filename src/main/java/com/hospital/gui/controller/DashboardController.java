package com.hospital.gui.controller;

import com.hospital.service.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardController {
    @FXML private Label userLabel;
    @FXML private StackPane contentPane;

    private AuthService authService;
    private PatientService patientService;
    private AppointmentService appointmentService;
    private BillingService billingService;
    private MedicalRecordService medicalRecordService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }

    public void setMedicalRecordService(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @FXML
    public void initialize() {
        if (authService != null && authService.getCurrentUser() != null) {
            userLabel.setText("User: " + authService.getCurrentUser().getUsername() +
                            " (" + authService.getCurrentUser().getRole() + ")");
        }
    }

    @FXML
    private void onLogout() {
        authService.logout();
        Stage stage = (Stage) userLabel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void openPatients() {
        loadView("/fxml/patients.fxml", controller -> {
            if (controller instanceof PatientsController) {
                ((PatientsController) controller).setPatientService(patientService);
                ((PatientsController) controller).setMedicalRecordService(medicalRecordService);
            }
        });
    }

    @FXML
    private void openAppointments() {
        loadView("/fxml/appointments.fxml", controller -> {
            if (controller instanceof AppointmentsController) {
                ((AppointmentsController) controller).setAppointmentService(appointmentService);
                ((AppointmentsController) controller).setPatientService(patientService);
            }
        });
    }

    @FXML
    private void openBilling() {
        loadView("/fxml/billing.fxml", controller -> {
            if (controller instanceof BillingController) {
                ((BillingController) controller).setBillingService(billingService);
                ((BillingController) controller).setPatientService(patientService);
            }
        });
    }

    @FXML
    private void openMedicalRecords() {
        // Could load a medical records view here
        System.out.println("Medical Records clicked");
    }

    private void loadView(String fxmlPath, java.util.function.Consumer<Object> controllerConfig) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            Object controller = loader.getController();

            if (controllerConfig != null) {
                controllerConfig.accept(controller);
            }

            if (controller instanceof BaseController) {
                ((BaseController) controller).initialize();
            }

            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + e.getMessage());
        }
    }
}
