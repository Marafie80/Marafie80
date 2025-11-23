package com.hospital.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Visit {
    private String id;
    private LocalDateTime date;
    private String doctorId;
    private String notes;
    private List<Prescription> prescriptions = new ArrayList<>();

    public Visit() {
    }

    public Visit(String id, LocalDateTime date, String doctorId, String notes) {
        this.id = id;
        this.date = date;
        this.doctorId = doctorId;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }
}
