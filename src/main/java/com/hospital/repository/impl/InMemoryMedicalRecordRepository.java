package com.hospital.repository.impl;

import com.hospital.model.MedicalRecord;
import com.hospital.repository.CrudRepository;

import java.util.*;

public class InMemoryMedicalRecordRepository implements CrudRepository<MedicalRecord, String> {
    private final Map<String, MedicalRecord> db = new HashMap<>();

    @Override
    public MedicalRecord save(MedicalRecord record) {
        if (record.getId() == null || record.getId().isEmpty()) {
            record.setId(UUID.randomUUID().toString());
        }
        db.put(record.getId(), record);
        return record;
    }

    @Override
    public Optional<MedicalRecord> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<MedicalRecord> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public Optional<MedicalRecord> findByPatientId(String patientId) {
        return db.values().stream()
                .filter(r -> r.getPatientId().equals(patientId))
                .findFirst();
    }
}
