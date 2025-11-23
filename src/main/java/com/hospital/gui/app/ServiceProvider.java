package com.hospital.gui.app;

import com.hospital.service.*;

/**
 * Singleton service provider to share services across controllers
 */
public class ServiceProvider {
    private static ServiceProvider instance;

    private AuthService authService;
    private PatientService patientService;
    private AppointmentService appointmentService;
    private BillingService billingService;
    private MedicalRecordService medicalRecordService;

    private ServiceProvider() {
    }

    public static ServiceProvider getInstance() {
        if (instance == null) {
            instance = new ServiceProvider();
        }
        return instance;
    }

    public void initialize(AuthService authService, PatientService patientService,
                         AppointmentService appointmentService, BillingService billingService,
                         MedicalRecordService medicalRecordService) {
        this.authService = authService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.billingService = billingService;
        this.medicalRecordService = medicalRecordService;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public PatientService getPatientService() {
        return patientService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    public BillingService getBillingService() {
        return billingService;
    }

    public MedicalRecordService getMedicalRecordService() {
        return medicalRecordService;
    }
}
