package com.hospital.model;

import java.time.LocalDateTime;

public class Appointment {
    private String id;
    private String patientId;
    private String staffId; // doctor or nurse
    private LocalDateTime start;
    private LocalDateTime end;
    private AppointmentStatus status;
    private String notes;

    public Appointment() {
    }

    public Appointment(String id, String patientId, String staffId, LocalDateTime start, LocalDateTime end, AppointmentStatus status) {
        this.id = id;
        this.patientId = patientId;
        this.staffId = staffId;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
