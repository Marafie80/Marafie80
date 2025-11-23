package com.hospital.repository.impl;

import com.hospital.model.Patient;
import com.hospital.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryPatientRepository implements CrudRepository<Patient, String> {
    private final Map<String, Patient> db = new HashMap<>();

    @Override
    public Patient save(Patient patient) {
        if (patient.getId() == null || patient.getId().isEmpty()) {
            patient.setId(UUID.randomUUID().toString());
        }
        db.put(patient.getId(), patient);
        return patient;
    }

    @Override
    public Optional<Patient> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Patient> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public List<Patient> search(String query) {
        String lowerQuery = query.toLowerCase();
        return db.values().stream()
                .filter(p -> p.getFirstName().toLowerCase().contains(lowerQuery) ||
                            p.getLastName().toLowerCase().contains(lowerQuery) ||
                            (p.getContact() != null && p.getContact().getPhone().contains(query)))
                .collect(Collectors.toList());
    }
}
