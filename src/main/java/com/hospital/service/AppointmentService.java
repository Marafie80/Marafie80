package com.hospital.service;

import com.hospital.exception.NotFoundException;
import com.hospital.exception.SchedulingConflictException;
import com.hospital.exception.ValidationException;
import com.hospital.model.Appointment;
import com.hospital.model.AppointmentStatus;
import com.hospital.repository.impl.InMemoryAppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {
    private final InMemoryAppointmentRepository appointmentRepository;

    public AppointmentService(InMemoryAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment schedule(String patientId, String staffId, LocalDateTime start, LocalDateTime end) {
        validateAppointmentTimes(start, end);
        checkForConflicts(staffId, start, end, null);

        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setStaffId(staffId);
        appointment.setStart(start);
        appointment.setEnd(end);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }

    public Appointment update(Appointment appointment) {
        if (appointment.getId() == null) {
            throw new ValidationException("Appointment ID is required for update");
        }

        appointmentRepository.findById(appointment.getId())
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + appointment.getId()));

        validateAppointmentTimes(appointment.getStart(), appointment.getEnd());
        checkForConflicts(appointment.getStaffId(), appointment.getStart(), appointment.getEnd(), appointment.getId());

        return appointmentRepository.save(appointment);
    }

    public void cancel(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + appointmentId));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    public void complete(String appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + appointmentId));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> findByPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public List<Appointment> findByStaff(String staffId) {
        return appointmentRepository.findByStaffId(staffId);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(String id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appointment not found: " + id));
    }

    private void validateAppointmentTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ValidationException("Start and end times are required");
        }
        if (!start.isBefore(end)) {
            throw new ValidationException("Start time must be before end time");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Cannot schedule appointments in the past");
        }
    }

    private void checkForConflicts(String staffId, LocalDateTime start, LocalDateTime end, String excludeAppointmentId) {
        for (Appointment existing : appointmentRepository.findAll()) {
            if (excludeAppointmentId != null && existing.getId().equals(excludeAppointmentId)) {
                continue;
            }

            if (existing.getStaffId().equals(staffId) && existing.getStatus() == AppointmentStatus.SCHEDULED) {
                boolean overlap = start.isBefore(existing.getEnd()) && end.isAfter(existing.getStart());
                if (overlap) {
                    throw new SchedulingConflictException(
                        "Staff member already has an appointment from " + existing.getStart() + " to " + existing.getEnd()
                    );
                }
            }
        }
    }
}
