package com.hospital.model;

import java.time.LocalDateTime;

public class LabTest {
    private String id;
    private String patientId;
    private String testType;
    private String result;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;

    public LabTest() {
    }

    public LabTest(String id, String patientId, String testType) {
        this.id = id;
        this.patientId = patientId;
        this.testType = testType;
        this.requestedAt = LocalDateTime.now();
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public boolean isCompleted() {
        return completedAt != null && result != null;
    }
}
