package com.hospital.service;

import com.hospital.exception.NotFoundException;
import com.hospital.exception.ValidationException;
import com.hospital.model.Patient;
import com.hospital.repository.impl.InMemoryPatientRepository;

import java.util.List;

public class PatientService {
    private final InMemoryPatientRepository patientRepository;

    public PatientService(InMemoryPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient create(Patient patient) {
        validatePatient(patient);
        return patientRepository.save(patient);
    }

    public Patient update(Patient patient) {
        if (patient.getId() == null) {
            throw new ValidationException("Patient ID is required for update");
        }
        patientRepository.findById(patient.getId())
                .orElseThrow(() -> new NotFoundException("Patient not found: " + patient.getId()));
        validatePatient(patient);
        return patientRepository.save(patient);
    }

    public Patient findById(String id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found: " + id));
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public List<Patient> search(String query) {
        if (query == null || query.isEmpty()) {
            return findAll();
        }
        return patientRepository.search(query);
    }

    public void delete(String id) {
        patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found: " + id));
        patientRepository.deleteById(id);
    }

    private void validatePatient(Patient patient) {
        if (patient.getFirstName() == null || patient.getFirstName().isEmpty()) {
            throw new ValidationException("First name is required");
        }
        if (patient.getLastName() == null || patient.getLastName().isEmpty()) {
            throw new ValidationException("Last name is required");
        }
        if (patient.getDob() == null) {
            throw new ValidationException("Date of birth is required");
        }
    }
}
