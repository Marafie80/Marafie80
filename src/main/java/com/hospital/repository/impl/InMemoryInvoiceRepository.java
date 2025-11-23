package com.hospital.repository.impl;

import com.hospital.model.Invoice;
import com.hospital.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryInvoiceRepository implements CrudRepository<Invoice, String> {
    private final Map<String, Invoice> db = new HashMap<>();

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice.getId() == null || invoice.getId().isEmpty()) {
            invoice.setId(UUID.randomUUID().toString());
        }
        db.put(invoice.getId(), invoice);
        return invoice;
    }

    @Override
    public Optional<Invoice> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<Invoice> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public List<Invoice> findByPatientId(String patientId) {
        return db.values().stream()
                .filter(i -> i.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
}
