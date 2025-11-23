package com.hospital.repository.impl;

import com.hospital.model.Appointment;
import com.hospital.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryAppointmentRepository implements CrudRepository<Appointment, String> {
    private final Map<String, Appointment> db = new HashMap<>();

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getId() == null || appointment.getId().isEmpty()) {
            appointment.setId(UUID.randomUUID().toString());
        }
        db.put(appointment.getId(), appointment);
        return appointment;
    }

    @Override
    public Optional<Appointment> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public List<Appointment> findByPatientId(String patientId) {
        return db.values().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> findByStaffId(String staffId) {
        return db.values().stream()
                .filter(a -> a.getStaffId().equals(staffId))
                .collect(Collectors.toList());
    }
}
