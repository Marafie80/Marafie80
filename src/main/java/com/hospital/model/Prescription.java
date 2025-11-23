package com.hospital.model;

public class Prescription {
    private String id;
    private String medication;
    private String dosage;
    private int durationDays;

    public Prescription() {
    }

    public Prescription(String id, String medication, String dosage, int durationDays) {
        this.id = id;
        this.medication = medication;
        this.dosage = dosage;
        this.durationDays = durationDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(int durationDays) {
        this.durationDays = durationDays;
    }
}
