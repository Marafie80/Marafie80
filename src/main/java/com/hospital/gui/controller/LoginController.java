package com.hospital.gui.controller;

import com.hospital.gui.app.ServiceProvider;
import com.hospital.service.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private AuthService authService;

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            boolean authenticated = authService.authenticate(username, password);
            if (authenticated) {
                openDashboard();
                closeLoginWindow();
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            errorLabel.setText("Login error: " + e.getMessage());
        }
    }

    @FXML
    private void onExit() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void openDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 600);

            DashboardController controller = loader.getController();
            ServiceProvider services = ServiceProvider.getInstance();
            controller.setAuthService(services.getAuthService());
            controller.setPatientService(services.getPatientService());
            controller.setAppointmentService(services.getAppointmentService());
            controller.setBillingService(services.getBillingService());
            controller.setMedicalRecordService(services.getMedicalRecordService());
            controller.initialize();

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Hospital Dashboard");
            stage.show();
        } catch (Exception e) {
            errorLabel.setText("Error opening dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeLoginWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
