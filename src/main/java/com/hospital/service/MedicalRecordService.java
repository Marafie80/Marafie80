package com.hospital.service;

import com.hospital.exception.NotFoundException;
import com.hospital.exception.ValidationException;
import com.hospital.model.MedicalRecord;
import com.hospital.model.Prescription;
import com.hospital.model.Visit;
import com.hospital.repository.impl.InMemoryMedicalRecordRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class MedicalRecordService {
    private final InMemoryMedicalRecordRepository recordRepository;

    public MedicalRecordService(InMemoryMedicalRecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public MedicalRecord createRecord(String patientId) {
        if (patientId == null || patientId.isEmpty()) {
            throw new ValidationException("Patient ID is required");
        }

        // Check if record already exists
        if (recordRepository.findByPatientId(patientId).isPresent()) {
            throw new ValidationException("Medical record already exists for patient: " + patientId);
        }

        MedicalRecord record = new MedicalRecord();
        record.setPatientId(patientId);
        return recordRepository.save(record);
    }

    public MedicalRecord getByPatientId(String patientId) {
        return recordRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NotFoundException("Medical record not found for patient: " + patientId));
    }

    public Visit addVisit(String patientId, String doctorId, String notes) {
        MedicalRecord record = getByPatientId(patientId);

        Visit visit = new Visit();
        visit.setId(UUID.randomUUID().toString());
        visit.setDate(LocalDateTime.now());
        visit.setDoctorId(doctorId);
        visit.setNotes(notes);

        record.getVisits().add(visit);
        recordRepository.save(record);

        return visit;
    }

    public void addPrescription(String patientId, String visitId, String medication, String dosage, int durationDays) {
        MedicalRecord record = getByPatientId(patientId);

        Visit visit = record.getVisits().stream()
                .filter(v -> v.getId().equals(visitId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Visit not found: " + visitId));

        validatePrescription(medication, dosage, durationDays);

        Prescription prescription = new Prescription();
        prescription.setId(UUID.randomUUID().toString());
        prescription.setMedication(medication);
        prescription.setDosage(dosage);
        prescription.setDurationDays(durationDays);

        visit.getPrescriptions().add(prescription);
        recordRepository.save(record);
    }

    private void validatePrescription(String medication, String dosage, int durationDays) {
        if (medication == null || medication.isEmpty()) {
            throw new ValidationException("Medication name is required");
        }
        if (dosage == null || dosage.isEmpty()) {
            throw new ValidationException("Dosage is required");
        }
        if (durationDays <= 0) {
            throw new ValidationException("Duration must be positive");
        }
    }
}
