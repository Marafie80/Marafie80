package com.hospital.repository.impl;

import com.hospital.model.LabTest;
import com.hospital.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryLabTestRepository implements CrudRepository<LabTest, String> {
    private final Map<String, LabTest> db = new HashMap<>();

    @Override
    public LabTest save(LabTest test) {
        if (test.getId() == null || test.getId().isEmpty()) {
            test.setId(UUID.randomUUID().toString());
        }
        db.put(test.getId(), test);
        return test;
    }

    @Override
    public Optional<LabTest> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<LabTest> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public List<LabTest> findByPatientId(String patientId) {
        return db.values().stream()
                .filter(t -> t.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<LabTest> findPending() {
        return db.values().stream()
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());
    }
}
