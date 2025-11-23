package com.hospital.model;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private String id;
    private String patientId;
    private List<Visit> visits = new ArrayList<>();

    public MedicalRecord() {
    }

    public MedicalRecord(String id, String patientId) {
        this.id = id;
        this.patientId = patientId;
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

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }
}
