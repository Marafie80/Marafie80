package com.hospital.gui.app;

import com.hospital.gui.controller.DashboardController;
import com.hospital.gui.controller.LoginController;
import com.hospital.model.Role;
import com.hospital.repository.impl.*;
import com.hospital.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    // Repositories
    private InMemoryUserRepository userRepository;
    private InMemoryPatientRepository patientRepository;
    private InMemoryDoctorRepository doctorRepository;
    private InMemoryAppointmentRepository appointmentRepository;
    private InMemoryMedicalRecordRepository medicalRecordRepository;
    private InMemoryInvoiceRepository invoiceRepository;
    private InMemoryInventoryRepository inventoryRepository;
    private InMemoryLabTestRepository labTestRepository;

    // Services
    private AuthService authService;
    private PatientService patientService;
    private AppointmentService appointmentService;
    private BillingService billingService;
    private MedicalRecordService medicalRecordService;

    @Override
    public void init() throws Exception {
        // Initialize repositories
        userRepository = new InMemoryUserRepository();
        patientRepository = new InMemoryPatientRepository();
        doctorRepository = new InMemoryDoctorRepository();
        appointmentRepository = new InMemoryAppointmentRepository();
        medicalRecordRepository = new InMemoryMedicalRecordRepository();
        invoiceRepository = new InMemoryInvoiceRepository();
        inventoryRepository = new InMemoryInventoryRepository();
        labTestRepository = new InMemoryLabTestRepository();

        // Initialize services
        authService = new AuthService(userRepository);
        patientService = new PatientService(patientRepository);
        appointmentService = new AppointmentService(appointmentRepository);
        billingService = new BillingService(invoiceRepository);
        medicalRecordService = new MedicalRecordService(medicalRecordRepository);

        // Initialize service provider
        ServiceProvider.getInstance().initialize(authService, patientService, appointmentService,
                billingService, medicalRecordService);

        // Create default admin user
        createDefaultUsers();
    }

    private void createDefaultUsers() {
        try {
            authService.register("admin", "admin123", Role.ADMIN);
            authService.register("doctor", "doctor123", Role.DOCTOR);
            authService.register("nurse", "nurse123", Role.NURSE);
            authService.register("receptionist", "reception123", Role.RECEPTIONIST);
        } catch (Exception e) {
            System.err.println("Error creating default users: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        // Custom controller factory to inject dependencies
        loader.setControllerFactory(clazz -> {
            try {
                Object controller = clazz.getDeclaredConstructor().newInstance();

                if (controller instanceof LoginController) {
                    LoginController loginController = (LoginController) controller;
                    loginController.setAuthService(authService);
                    // Pass services to be used after login
                    return loginController;
                }

                if (controller instanceof DashboardController) {
                    DashboardController dashboardController = (DashboardController) controller;
                    dashboardController.setAuthService(authService);
                    dashboardController.setPatientService(patientService);
                    dashboardController.setAppointmentService(appointmentService);
                    dashboardController.setBillingService(billingService);
                    dashboardController.setMedicalRecordService(medicalRecordService);
                    return dashboardController;
                }

                return controller;
            } catch (Exception e) {
                throw new RuntimeException("Error creating controller: " + clazz.getName(), e);
            }
        });

        Scene scene = new Scene(loader.load(), 450, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hospital Management System - Login");
        primaryStage.setResizable(false);
        primaryStage.show();

        // Print default credentials to console
        System.out.println("===========================================");
        System.out.println("Hospital Management System");
        System.out.println("===========================================");
        System.out.println("Default Login Credentials:");
        System.out.println("  Admin:        admin / admin123");
        System.out.println("  Doctor:       doctor / doctor123");
        System.out.println("  Nurse:        nurse / nurse123");
        System.out.println("  Receptionist: receptionist / reception123");
        System.out.println("===========================================");
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Getters for services (if needed by controllers)
    public AuthService getAuthService() { return authService; }
    public PatientService getPatientService() { return patientService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public BillingService getBillingService() { return billingService; }
    public MedicalRecordService getMedicalRecordService() { return medicalRecordService; }
}
