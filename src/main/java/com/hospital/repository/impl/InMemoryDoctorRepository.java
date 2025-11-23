package com.hospital.repository.impl;

import com.hospital.model.Doctor;
import com.hospital.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryDoctorRepository implements CrudRepository<Doctor, String> {
    private final Map<String, Doctor> db = new HashMap<>();

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor.getId() == null || doctor.getId().isEmpty()) {
            doctor.setId(UUID.randomUUID().toString());
        }
        db.put(doctor.getId(), doctor);
        return doctor;
    }

    @Override
    public Optional<Doctor> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Doctor> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public List<Doctor> findBySpecialty(String specialty) {
        return db.values().stream()
                .filter(d -> d.getSpecialty().equalsIgnoreCase(specialty))
                .collect(Collectors.toList());
    }
}
